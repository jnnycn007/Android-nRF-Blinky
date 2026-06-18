plugins {
    alias(libs.plugins.kotlin.serialization) apply false

    // Nordic plugins are defined in https://github.com/nordicsemi/Nordic-Gradle-Plugins
    alias(libs.plugins.nordic.android.application) apply false
    alias(libs.plugins.nordic.android.library) apply false
    alias(libs.plugins.nordic.kotlin) apply false
    alias(libs.plugins.nordic.feature.compose) apply false
    alias(libs.plugins.nordic.feature.hilt) apply false
    alias(libs.plugins.nordic.feature.hilt.compose) apply false
}