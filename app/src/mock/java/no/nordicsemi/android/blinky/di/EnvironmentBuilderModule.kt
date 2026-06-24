package no.nordicsemi.android.blinky.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.nordicsemi.kotlin.ble.environment.android.mock.MockAndroidEnvironment
import javax.inject.Singleton

/**
 * This module provides the Environment in which the app is running.
 */
@Module
@InstallIn(SingletonComponent::class)
internal class EnvironmentBuilderModule {

    @Provides
    @Singleton
    internal fun provideEnvironmentBuilder() = EnvironmentBuilder {
        MockAndroidEnvironment.Api31(
            isBluetoothEnabled = true,
            isBluetoothConnectPermissionGranted = true,
            isBluetoothScanPermissionGranted = true,
            isNeverForLocationFlagSet = true,
        )
    }
}