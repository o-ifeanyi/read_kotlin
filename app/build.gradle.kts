plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("/Users/ifeanyionuoha/read_keystore.jks")
            storePassword = "readkeystore"
            keyPassword = "readkeystore"
            keyAlias = "release"
        }
    }
    namespace = "com.ifeanyi.read"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ifeanyi.read"
        minSdk = 26
        targetSdk = 34
        versionCode = 13
        versionName = "1.1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
        }
    }
}

dependencies {
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    // noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // PDF to text
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
    // Image to text
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    // URL to text
    implementation("org.jsoup:jsoup:1.14.3")
    // Font
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.8")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // Material icons
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    // Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // GSON
    implementation("com.google.code.gson:gson:2.10.1")
    // Media notification
    implementation("androidx.media:media:1.7.0")
    // Mixpanel
    implementation("com.mixpanel.android:mixpanel-android:7.5.0")
    // Gemini
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    // Scanner
    implementation("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}