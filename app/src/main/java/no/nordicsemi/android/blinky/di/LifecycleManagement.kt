package no.nordicsemi.android.blinky.di

import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment

internal fun interface EnvironmentBuilder {
    /**
     * Creates the Environment to use in the app.
     *
     * This can be either a native, or a mock environment.
     */
    fun create(): AndroidEnvironment
}

internal fun interface CentralManagerBuilder {
    /**
     * Create a new [CentralManager] instance.
     *
     * This should return a native or a mock Central Manager, depending on the [environment] provided.
     *
     * This is called once for a lifetime of an app - when the first Activity or a Service is created.
     * The [CentralManager] is used to scan and connect to Bluetooth LE peripherals.
     * The instance is [closed][CentralManager.close] when the last Activity or Service is destroyed.
     *
     *
     * @param environment The environment in which the app is running.
     * @param scope The scope to run connection on. The scope is canceled together with the returned
     * instance when the last Activity or Service is destroyed.
     */
    fun create(environment: AndroidEnvironment, scope: CoroutineScope): CentralManager
}