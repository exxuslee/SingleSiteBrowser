plugins {
    id("com.android.application") version "9.2.1" apply false
    id("org.jetbrains.kotlin.android") version "2.3.21" apply false
}

buildscript {
    repositories {
        maven {
            name = "Mozilla"
            url = uri("https://maven.mozilla.org/maven2")
        }
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.mozilla.components:tooling-glean-gradle:111.1.1")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
