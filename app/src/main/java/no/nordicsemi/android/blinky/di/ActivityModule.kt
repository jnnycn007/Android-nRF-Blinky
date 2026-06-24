package no.nordicsemi.android.blinky.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.ActivityRetainedLifecycle
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment

/**
 * This module provides [AndroidEnvironment] and [CentralManager] instances that are scoped to
 * the Activity lifecycle.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
internal class ActivityModule {

    @Provides
    @ActivityRetainedScoped
    fun provideBluetoothLifecycle(
        bluetoothLifecycleOwner: BluetoothLifecycleOwner,
        lifecycle: ActivityRetainedLifecycle,
    ): BluetoothLifecycle {
        lifecycle.addOnClearedListener {
            bluetoothLifecycleOwner.release()
        }
        return bluetoothLifecycleOwner.acquire()
    }

    @Provides
    fun provideEnvironment(
        bluetoothLifecycle: BluetoothLifecycle,
    ): AndroidEnvironment = bluetoothLifecycle.environment

    @Provides
    fun provideCentralManager(
        bluetoothLifecycle: BluetoothLifecycle,
    ): CentralManager = bluetoothLifecycle.centralManager
}