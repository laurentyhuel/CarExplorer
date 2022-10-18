plugins {
    id("carexplorer.android.library")
}

android {
    namespace = "com.lyh.carexplorer.domain"
}

dependencies {
    testImplementation(libs.turbine)
}