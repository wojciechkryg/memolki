import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kover)
}

kotlin {
    // Custom source sets disable the default hierarchy auto-detection,
    // so apply it explicitly to keep iosArm64/iosSimulatorArm64 → iosMain → commonMain.
    applyDefaultHierarchyTemplate()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.3.35"
        summary = "Memolki shared KMP module"
        homepage = "https://github.com/wojdorz/memolki"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "Shared"
            isStatic = false
        }
        pod("FirebaseCore") { version = "11.15.0" }
        pod("FirebaseAnalytics") { version = "11.15.0" }
        pod("FirebaseCrashlytics") { version = "11.15.0" }
        pod("FirebaseMessaging") { version = "11.15.0" }
        pod("FirebaseInstallations") { linkOnly = true }
        pod("GoogleUtilities") { linkOnly = true }
        pod("nanopb") { linkOnly = true }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
            implementation(libs.androidx.datastore.preferences.core)
            api(libs.androidx.room.runtime)
            api(libs.androidx.sqlite.bundled)
            api(libs.koin.core)
            api(libs.gitlive.firebase.analytics)
            api(libs.gitlive.firebase.crashlytics)
            api(libs.gitlive.firebase.messaging)
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(libs.androidx.navigation.compose)
            api(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.process)
        }
        iosMain.dependencies {
        }

    }
}

// Per-flavor sourceSets: android<Flavor>Main + iosMain (active flavor only) → <flavor>Main → commonMain.
// iOS active flavor is read from iosApp/.active-flavor (written by each Xcode scheme's pre-action
// before :shared:syncFramework runs). Using providers.fileContents() so the configuration cache
// invalidates when the file content changes, otherwise scheme switches would silently reuse stale config.
val activeFlavorFile = rootProject.layout.projectDirectory.file("iosApp/.active-flavor")
val activeIosFlavor: String =
    (System.getenv("IOS_FLAVOR")
        ?: providers.gradleProperty("iosFlavor").orNull
        ?: providers.fileContents(activeFlavorFile).asText.orNull?.trim()?.takeIf { it.isNotEmpty() })
        ?.replaceFirstChar(Char::lowercaseChar)
        ?: "fruitHalf"

afterEvaluate {
    val flavorNames = listOf("fruitHalf", "vegetableHalf", "mammalSide", "birdSide")
    kotlin.sourceSets.apply {
        flavorNames.forEach { flavor ->
            val flavorMain = maybeCreate("${flavor}Main").apply {
                dependsOn(getByName("commonMain"))
            }
            findByName("android${flavor.replaceFirstChar(Char::uppercaseChar)}")?.dependsOn(flavorMain)
            if (flavor == activeIosFlavor) {
                getByName("iosMain").dependsOn(flavorMain)
            }
        }
    }
}

android {
    namespace = "com.wojdor.memolki.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    flavorDimensions += "version"
    productFlavors {
        create("fruitHalf") { dimension = "version" }
        create("vegetableHalf") { dimension = "version" }
        create("mammalSide") { dimension = "version" }
        create("birdSide") { dimension = "version" }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.wojdor.memolki.shared.resources"
    generateResClass = auto
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}
