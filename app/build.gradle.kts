plugins {
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.android.application.plugin)
    alias(libs.plugins.kotlin.android.plugin)
}

android {
    namespace = "ru.antares.cheese_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.antares.cheese_android"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

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
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // region - Default implementations
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.compose.bom)

    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)

    implementation(libs.junit)
    implementation(libs.test.core)
    implementation(libs.test.ext.junit)

    implementation(libs.espresso.core)
    implementation(libs.compose.bom)

    implementation(libs.lifecycle.runtime.compose)
    // endregion

    // region - Country code picker
    implementation(libs.material)
    implementation(libs.jetpack.compose.country.code.picker.emoji)
    // endregion

    // region - Retrofit2
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    // endregion

    // region - kotlinx.serialization
    implementation(libs.kotlinx.serialization)
    // endregion

    // region - Dependency Injection (Koin)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    // endregion

    // region - OkHttp3
    implementation(libs.okhttp)
    // endregion

    // region - MockWebServer
    testImplementation(libs.mockwebserver)
    // endregion

    // region - DataStore
    implementation(libs.datastore.preferences)
    // endregion

    // region - Gson
    implementation(libs.gson.converter)
    implementation(libs.gson)
    // endregion

    // region - navigation
    implementation(libs.navigation.compose)
    // endregion

    // region - Room data base
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    debugImplementation(libs.ui.tooling)
    ksp(libs.room.compiler)
    // endregion

    // region - Coil Async Image
    implementation(libs.coil.compose)
    // endregion

    // region - Arrow
    implementation(libs.arrow.core)
    implementation(libs.arrow.optics)
    ksp(libs.arrow.optics.ksp.plugin)
}