plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // ✅ required for Kotlin 2.0
}

android {
    namespace = "com.example.torchapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.torchapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // ✅ match with Compose 1.5.x or bump to 1.6.x if using Compose BOM
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // ✅ Activity Result API (for image picker)
    implementation("androidx.activity:activity-compose:1.9.0") // Latest stable version

    // ✅ Compose UI + Material 3
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui:1.4.0" )// or latest stable version
    implementation( "androidx.compose.material3:material3:1.1.0")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.runtime:runtime:1.3.0") // Corrected version to match your Compose setup

    // ✅ PyTorch
    implementation("org.pytorch:pytorch_android:1.13.1")
    implementation("org.pytorch:pytorch_android_torchvision:1.13.1")

    // ✅ Image picker support
    implementation("androidx.activity:activity-ktx:1.9.0") // Use latest version for activity-related APIs

    // Debugging tools
    debugImplementation("androidx.compose.ui:ui-tooling")
}
