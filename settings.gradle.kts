pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CarExplorer"

include(":app")
include(":data:data-local")
include(":data:data-remote")
include(":data")
include(":domain")
include(":app:feature-car")
include(":app:feature-core")
include(":app:feature-user")
