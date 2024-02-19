// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application.plugin) apply false
    //id("com.android.application") version "8.2.2" apply false

    alias(libs.plugins.kotlin.android.plugin) apply false
    //id("org.jetbrains.kotlin.android") version "1.9.20" apply false

    alias(libs.plugins.serialization.plugin) apply false
    //id ("org.jetbrains.kotlin.plugin.serialization") version "1.6.10" apply false

    alias(libs.plugins.ksp.plugin) apply false
    //id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
}