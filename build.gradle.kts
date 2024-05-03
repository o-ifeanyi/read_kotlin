// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    // Kotlin
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    // Hilt
    id("com.google.dagger.hilt.android") version "2.48" apply false
    // Read stuff from local.properties
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}
