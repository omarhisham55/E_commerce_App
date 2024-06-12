buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46")
        classpath("com.google.gms:google-services:4.4.2")
        val navVersion = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}


plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.android.library") version "7.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}