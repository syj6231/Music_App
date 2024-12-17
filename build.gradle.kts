plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript {
    repositories {
        google()        // Google Maven 저장소
        mavenCentral()  // Maven Central 저장소
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1") // Android Gradle 플러그인
        classpath("com.google.gms:google-services:4.4.0") // Google Play 서비스
    }
}
