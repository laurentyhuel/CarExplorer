plugins {
    id("carexplorer.android.library")
}

android {
    namespace = "com.lyh.carexplorer.data"
}

dependencies {

    implementation(libs.retrofit.core)
    implementation(project(":data:data-remote"))
    implementation(project(":data:data-local"))
    implementation(project(":domain"))

    testImplementation(libs.turbine)
}