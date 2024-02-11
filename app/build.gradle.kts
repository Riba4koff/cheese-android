plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("kotlin-kapt")
    id("com.google.devtools.ksp")
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // region - Default implementations
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    // endregion

    // region - Country code picker
    implementation("androidx.compose.material:material:1.6.0")
    implementation("com.github.jump-sdk:jetpack_compose_country_code_picker_emoji:2.2.6")
    // endregion

    // region - Retrofit2
    val retrofitVersion = "2.9.0"
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // endregion

    // region - kotlinx.serialization
    val kotlinxSerializationVersion = "1.6.2"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    // endregion

    // region - Dependency Injection (Koin)
    val diVersion = "3.5.0"
    implementation("io.insert-koin:koin-core:3.5.0")
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    // endregion

    // region - OkHttp3
    val okhttp3Version = "4.11.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp3Version")
    // endregion

    // region - MockWebServer
    val mockWebServerVersion = "4.9.0"
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockWebServerVersion")
    // endregion

    // region - DataStore
    val dataStoreVersion = "1.0.0"
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")
    // endregion

    // region - Gson
    val gsonVersion = "2.10"
    val gsonConverterVersion = "2.9.0"

    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.squareup.retrofit2:converter-gson:$gsonConverterVersion")
    // endregion

    // region - navigation
    val navVersion = "2.5.3"
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-compose:$navVersion")
    // endregion

    // region - Country Code Picker
    val ccPickerVersion = "2.2.6"
    implementation("com.github.jump-sdk:jetpack_compose_country_code_picker_emoji:$ccPickerVersion")
    // end region
    // region - Room data base
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    //kapt("androidx.room:room-compiler:$roomVersion") /* deprecated */
    // endregion
}