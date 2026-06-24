package no.nordicsemi.android.blinky.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment
import no.nordicsemi.kotlin.log.Log
import no.nordicsemi.kotlin.log.timber.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * An object that lives as long as the application is alive.
 */
internal interface BluetoothLifecycle {
    /** The Android environment object. */
    val environment: AndroidEnvironment
    /** Central Manager for the environment. */
    val centralManager: CentralManager
}

/**
 * An object that can acquire and release the [BluetoothLifecycle].
 */
internal interface BluetoothLifecycleOwner {
    /**
     * This method should be called if an `Activity` or `Service` needs to access the
     * [BluetoothLifecycle].
     */
    fun acquire(): BluetoothLifecycle

    /**
     * This method should be called if an `Activity` or `Service` is destroyed.
     */
    fun release()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BluetoothLifecycleOwnerModule {

    /**
     * A class that manages the lifecycle of the Bluetooth environment.
     *
     * It monitors the number of active activities and services in the app and
     * initializes the [AndroidEnvironment] and [CentralManager] when the
     * first one is created, and closes them when the last one is destroyed.
     */
    @Singleton
    internal class BluetoothLifecycleManager @Inject internal constructor(
        private val environmentBuilder: EnvironmentBuilder,
        private val centralManagerBuilder: CentralManagerBuilder,
    ): BluetoothLifecycleOwner, BluetoothLifecycle {
        private val activeCount = AtomicInteger(0)

        private var _environment: AndroidEnvironment? = null
        private var _scope: CoroutineScope? = null
        private var _centralManager: CentralManager? = null

        override val environment: AndroidEnvironment
            get() = synchronized(this) {
                requireNotNull(_environment) { "Bluetooth environment not initialized" }
            }

        override val centralManager: CentralManager
            get() = synchronized(this) {
                requireNotNull(_centralManager) { "CentralManager not initialized" }
            }

        override fun acquire(): BluetoothLifecycle {
            if (activeCount.getAndIncrement() == 0) {
                initialize()
            }
            return this
        }

        override fun release() {
            if (activeCount.decrementAndGet() == 0) {
                close()
            }
        }

        private fun initialize() {
            synchronized(this) {
                val env = environmentBuilder.create()
                val scp = CoroutineScope(SupervisorJob() + Dispatchers.IO)
                _environment = env
                _scope = scp
                _centralManager = centralManagerBuilder.create(env, scp)
                    .apply {
                        logger = Log.Sink.Timber { _, _-> true}
                    }
            }
        }

        private fun close() {
            synchronized(this) {
                _environment?.close()
                _centralManager?.close()
                _scope?.cancel()
                _environment = null
                _scope = null
                _centralManager = null
            }
        }
    }

    @Binds
    @Singleton
    internal abstract fun bindBluetoothLifecycleOwner(impl: BluetoothLifecycleManager): BluetoothLifecycleOwner
}