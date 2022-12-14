plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("customAndroidLibrary") {
            id = "carexplorer.android.library"
            implementationClass = "CustomAndroidLibraryPlugin"
        }
        register("CustomAndroidInstrumentedTest") {
            id = "carexplorer.android.instrumentedTest"
            implementationClass = "CustomAndroidInstrumentedTestPlugin"
        }
    }
}
