package no.nordicsemi.android.blinky.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.nordicsemi.kotlin.ble.environment.android.NativeAndroidEnvironment
import javax.inject.Singleton

/**
 * This module provides the Environment in which the app is running.
 */
@Module
@InstallIn(SingletonComponent::class)
internal class EnvironmentBuilderModule {

    @Provides
    @Singleton
    internal fun provideEnvironmentBuilder(
        @ApplicationContext context: Context,
    ) = EnvironmentBuilder {
        NativeAndroidEnvironment.getInstance(context, isNeverForLocationFlagSet = true)
    }
}