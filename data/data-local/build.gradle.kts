// Suppress needed until https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("carexplorer.android.library")
    id("carexplorer.android.instrumentedTest")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lyh.carexplorer.data.local"
}

dependencies {

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    androidTestImplementation(libs.turbine)
}