package no.nordicsemi.android.blinky.di

import android.app.Service
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import jakarta.inject.Inject
import no.nordicsemi.android.blinky.ble.BlinkyManager
import no.nordicsemi.android.blinky.spec.Blinky
import no.nordicsemi.android.blinky.ui.di.BlinkyFactory
import no.nordicsemi.kotlin.ble.client.android.CentralManager

@Module
@InstallIn(ServiceComponent::class)
internal abstract class ServiceModule {

    @ServiceScoped
    internal class BlinkyFactoryImpl @Inject constructor(
        private val bluetoothLifecycleOwner: BluetoothLifecycleOwner,
        service: Service,
    ): BlinkyFactory {
        private var centralManager: CentralManager? = null

        // This initiator registers a Service lifecycle observer to acquire the Bluetooth Lifecycle
        // Owner when the Service is created and release it when and destroyed.
        //
        // This is possible, because the service is injecting the BlinkyFactory. Otherwise, the
        // manager should be informed manually in onCreate and onDestroy methods.
        init {
            val lifecycle = (service as? LifecycleOwner)?.lifecycle
            lifecycle?.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(
                    source: LifecycleOwner,
                    event: Lifecycle.Event
                ) = when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        centralManager = bluetoothLifecycleOwner.acquire().centralManager
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        centralManager = null
                        bluetoothLifecycleOwner.release()
                    }
                    else -> {}
                }
            })
        }

        override fun create(identifier: String): Blinky {
            val centralManager = requireNotNull(centralManager) { "Central manager not initialized" }
            val peripheral = centralManager.getPeripheralById(identifier)!!
            return BlinkyManager(centralManager, peripheral)
        }
    }

    @Binds
    @ServiceScoped
    internal abstract fun bind(
        impl: BlinkyFactoryImpl
    ): BlinkyFactory
}