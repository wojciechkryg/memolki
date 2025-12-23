import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
}

val secretsPropertiesFile = rootProject.file("secrets.properties")

val properties = Properties()
if (secretsPropertiesFile.exists()) {
    properties.load(FileInputStream(secretsPropertiesFile))
}

fun getSecretValue(key: String): String = providers
    .environmentVariable(key)
    .orElse(providers.gradleProperty(key))
    .getOrElse(properties.getProperty(key, ""))

val flavorConfigs = listOf(
    "fruitHalf" to "FRUIT_HALF_BILLING_KEY",
    "vegetableHalf" to "VEGETABLE_HALF_BILLING_KEY",
    "mammalSide" to "MAMMAL_SIDE_BILLING_KEY",
    "birdSide" to "BIRD_SIDE_BILLING_KEY"
)

android {
    namespace = "com.wojdor.memolki"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.wojdor.memolki"
        minSdk = 23
        targetSdk = 36
        versionCode = 1003005
        versionName = "1.3.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("KEYSTORE_PATH")
            if (keystorePath != null) {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    val versionDimension = "version"
    flavorDimensions += versionDimension
    productFlavors {
        flavorConfigs.forEach { (name, billingKeyName) ->
            create(name) {
                dimension = versionDimension
                applicationIdSuffix = ".${name.lowercase()}"
                val billingKey = getSecretValue(billingKeyName)
                val quoted = "\"${billingKey.replace("\"", "\\\"")}\""
                buildConfigField("String", "BILLING_KEY", quoted)
            }
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    tasks.matching {
        it.name.contains("release", ignoreCase = true) &&
                (it.name.startsWith("assemble") || it.name.startsWith("bundle"))
    }.configureEach {
        doFirst {
            val taskName = name
            var flavorPart: String? = null
            if (taskName.startsWith("assemble")) {
                flavorPart = taskName.removePrefix("assemble").removeSuffix("Release")
            } else if (taskName.startsWith("bundle")) {
                flavorPart = taskName.removePrefix("bundle").removeSuffix("Release")
            }

            if (flavorPart != null) {
                val flavorName = flavorPart.replaceFirstChar { it.lowercaseChar() }
                val billingKeyName = flavorConfigs.find { it.first == flavorName }?.second
                if (billingKeyName != null) {
                    if (getSecretValue(billingKeyName).isBlank()) {
                        throw GradleException("$billingKeyName is required for release builds for flavor $flavorName")
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime)
    implementation(libs.material)
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.play.services.ads.api)
    implementation(libs.billing)
    implementation(libs.review.ktx)
    implementation(libs.app.update.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.games)
    implementation(libs.kotlinx.coroutines.play.services)

    debugImplementation(libs.androidx.ui.tooling)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
}
