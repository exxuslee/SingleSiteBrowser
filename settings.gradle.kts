pluginManagement {
    repositories {
        maven {
            name = "Mozilla"
            url = uri("https://maven.mozilla.org/maven2")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven {
            name = "Mozilla"
            url = uri("https://maven.mozilla.org/maven2")
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

include(":app")
