@file:OptIn(ExperimentalUuidApi::class)

package no.nordicsemi.android.blinky.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.client.android.mock.mock
import no.nordicsemi.kotlin.ble.environment.android.mock.MockAndroidEnvironment
import javax.inject.Singleton
import kotlin.uuid.ExperimentalUuidApi

/**
 * This module provides the Central Manager used to scan and connect to Bluetooth LE peripherals.
 */
@Module
@InstallIn(SingletonComponent::class)
internal class CentralManagerBuilderModule {

    @Provides
    @Singleton
    internal fun provideCentralManagerBuilder() = CentralManagerBuilder { environment, scope ->
            CentralManager.mock(environment = environment as MockAndroidEnvironment, scope = scope)
                .apply {
                    simulatePeripherals(listOf(blinky, beacon, hrm))
                }
        }
}