import com.android.build.api.variant.FilterConfiguration
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val localProperties = Properties()
val localFile = rootProject.file("local.properties")
if (localFile.exists()) {
    localFile.inputStream().use { localProperties.load(it) }
}

val kotlinVersion: String = findProperty("kotlin_version") as String

android {
    compileSdk = 37
    namespace = "com.cookiejarapps.android.smartcookieweb"

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    defaultConfig {
        applicationId = "ua.kiev.intersystems.edpro.warranty"
        minSdk = 26
        targetSdk = 36
        versionCode = 128
        versionName = "29.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SITE_URL", "\"https://equipify.com.ua/index.php/servis-ta-garantiya\"")
        buildConfigField("boolean", "JAVA_SCRIPT_ENABLED", "true")
        buildConfigField("boolean", "LAUNCH_IN_APP", "true")
        buildConfigField("boolean", "AUTO_FONT_SIZE", "true")
        buildConfigField("float", "FONT_SIZE_FACTOR", "1f")
        buildConfigField("boolean", "SWIPE_TO_REFRESH", "true")
        buildConfigField("boolean", "REMOTE_DEBUGGING", "false")
        buildConfigField("boolean", "SAFE_BROWSING", "true")
        buildConfigField("boolean", "TRACKING_PROTECTION", "true")
        buildConfigField("boolean", "TRUST_THIRD_PARTY_CERTS", "false")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProperties.getProperty("RELEASE_STORE_FILE")!!)
            storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD")
            keyAlias = localProperties.getProperty("RELEASE_KEY_ALIAS")
            keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD")
            enableV1Signing = true
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    flavorDimensions += "abi"

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "x86_64")
            isUniversalApk = false
        }
    }

    lint {
        abortOnError = false
    }
}

val abiBaseMap = mapOf(
    "arm64-v8a" to 1,
    "x86_64" to 2,
)

androidComponents {
    onVariants(selector().all()) { variant ->
        variant.outputs.forEach { output ->
            val abi = output.filters
                .find { it.filterType == FilterConfiguration.FilterType.ABI }
                ?.identifier

            if (abi != null) {
                val base = abiBaseMap[abi] ?: 0
                val orig = output.versionCode.get()
                output.versionCode.set(base * 1000 + orig)
            }
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")
    implementation("androidx.annotation:annotation:1.10.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    val mozComponentsVersion = "150.0"

    implementation("org.mozilla.components:concept-engine:$mozComponentsVersion")
    implementation("org.mozilla.components:concept-base:$mozComponentsVersion")
    implementation("org.mozilla.components:browser-engine-gecko:$mozComponentsVersion")
    implementation("org.mozilla.components:browser-state:$mozComponentsVersion")
    implementation("org.mozilla.components:browser-errorpages:$mozComponentsVersion")
    implementation("org.mozilla.components:feature-tabs:$mozComponentsVersion")
    implementation("org.mozilla.components:feature-session:$mozComponentsVersion")
    implementation("org.mozilla.components:feature-contextmenu:$mozComponentsVersion")
    implementation("org.mozilla.components:feature-app-links:$mozComponentsVersion")
    implementation("org.mozilla.components:feature-prompts:$mozComponentsVersion")
    implementation("org.mozilla.components:feature-sitepermissions:$mozComponentsVersion")
    implementation("org.mozilla.components:feature-webcompat:$mozComponentsVersion")
    implementation("org.mozilla.components:support-base:$mozComponentsVersion")
    implementation("org.mozilla.components:support-utils:$mozComponentsVersion")
    implementation("org.mozilla.components:support-ktx:$mozComponentsVersion")
    implementation("org.mozilla.components:ui-widgets:$mozComponentsVersion")
    implementation("org.mozilla.components:lib-state:$mozComponentsVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}

configurations.configureEach {
    resolutionStrategy.capabilitiesResolution.withCapability("org.mozilla.telemetry:glean-native") {
        val toBeSelected = candidates.find { candidate ->
            val id = candidate.id
            id is ModuleComponentIdentifier && id.module.contains("geckoview")
        }
        if (toBeSelected != null) {
            select(toBeSelected)
        }
        because("use GeckoView Glean instead of standalone Glean")
    }
}
