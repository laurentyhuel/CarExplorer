import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("carexplorer.android.library")
    id("kotlinx-serialization")
}


val localProperties = gradleLocalProperties(rootDir)

android {
    namespace = "com.lyh.carexplorer.data.remote"
}

dependencies {
    implementation(libs.retrofit.core)

    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    implementation(libs.kotlinx.serialization.json)
}