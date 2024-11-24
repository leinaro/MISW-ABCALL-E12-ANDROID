// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.googleGmsGoogleServices) apply false
    // alias(libs.plugins.kotlinAndroidKsp) apply false
}