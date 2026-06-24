package no.nordicsemi.android.blinky.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.client.android.native
import no.nordicsemi.kotlin.ble.environment.android.NativeAndroidEnvironment
import javax.inject.Singleton

/**
 * This module provides the Central Manager used to scan and connect to Bluetooth LE peripherals.
 */
@Module
@InstallIn(SingletonComponent::class)
internal class CentralManagerBuilderModule {

    @Provides
    @Singleton
    internal fun provideCentralManagerBuilder() =
        CentralManagerBuilder { environment, scope ->
            CentralManager.native(environment = environment as NativeAndroidEnvironment, scope = scope)
        }
}