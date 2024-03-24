pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://foursquare.jfrog.io/foursquare/libs-release/")
        }
    }
}

rootProject.name = "Deserve -Near Restaurant"
include(":app")
