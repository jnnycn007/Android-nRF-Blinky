plugins {
    // https://github.com/nordicsemi/Nordic-Gradle-Plugins/blob/main/plugins/src/main/kotlin/AndroidLibraryConventionPlugin.kt
    alias(libs.plugins.nordic.android.library)
    // https://github.com/nordicsemi/Nordic-Gradle-Plugins/blob/main/plugins/src/main/kotlin/HiltComposeConventionPlugin.kt
    alias(libs.plugins.nordic.feature.hilt.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "no.nordicsemi.android.scanner"
}

dependencies {
    // The Bluetooth LE scanner is using Nordic common scanner from
    // github.com/nordicsemi/Android-Common-Libraries -> :scanner-ble
    implementation(nordic.scanner.ble)
    // This module contains a Nav3 entry point.
    implementation(libs.androidx.navigation3.runtime)
}