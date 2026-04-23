import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics.plugin)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.play.publisher)
    alias(libs.plugins.kover)
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

play {
    val serviceAccountPath = getSecretValue("PLAY_SERVICE_ACCOUNT_PATH")
    if (serviceAccountPath.isNotBlank()) {
        serviceAccountCredentials.set(file(serviceAccountPath))
    }
    track.set("production")
}

android {
    namespace = "com.wojdor.memolki"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.wojdor.memolki"
        minSdk = 23
        targetSdk = 36
        versionCode = 1003035
        versionName = "1.3.35"

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
        isCoreLibraryDesugaringEnabled = true
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
        unitTests.isIncludeAndroidResources = true
    }


    val resolvedBillingKeys = flavorConfigs.associate { (flavor, keyName) ->
        flavor to getSecretValue(keyName)
    }
    tasks.matching {
        it.name.contains("release", ignoreCase = true) &&
                (it.name.startsWith("assemble") || it.name.startsWith("bundle"))
    }.configureEach {
        val flavorPart = name.removeSuffix("Release").let {
            when {
                it.startsWith("assemble") -> it.removePrefix("assemble")
                it.startsWith("bundle") -> it.removePrefix("bundle")
                else -> null
            }
        }
        if (flavorPart != null) {
            val flavorName = flavorPart.replaceFirstChar { it.lowercaseChar() }
            val billingKey = resolvedBillingKeys[flavorName]
            if (billingKey != null && billingKey.isBlank()) {
                val billingKeyName = flavorConfigs.find { it.first == flavorName }?.second
                doFirst {
                    throw GradleException("$billingKeyName is required for release builds for flavor $flavorName")
                }
            }
        }
    }
}

val composeResourcesMirrorDir = layout.buildDirectory.dir("generated/composeResourcesMirror/res")

val mirrorComposeResourcesToAndroid by tasks.registering {
    description = "Copies compose-resources values*/strings*.xml and drawable*/ into Android res so R.string/R.drawable mirror Res.string/Res.drawable."
    group = "build"
    val srcDir = project(":shared").file("src/commonMain/composeResources")
    val outDir = composeResourcesMirrorDir
    inputs.dir(srcDir)
    outputs.dir(outDir)
    doLast {
        fun escape(content: String): String {
            val out = StringBuilder(content.length + 32)
            var i = 0
            while (i < content.length) {
                val ch = content[i]
                when {
                    ch == '<' && i + 1 < content.length && content[i + 1] == '?' -> {
                        val end = content.indexOf("?>", i)
                        if (end < 0) { out.append(content.substring(i)); return out.toString() }
                        out.append(content, i, end + 2); i = end + 2
                    }
                    ch == '<' && content.regionMatches(i, "<!--", 0, 4) -> {
                        val end = content.indexOf("-->", i)
                        if (end < 0) { out.append(content.substring(i)); return out.toString() }
                        out.append(content, i, end + 3); i = end + 3
                    }
                    ch == '<' -> {
                        val end = content.indexOf('>', i)
                        if (end < 0) { out.append(content.substring(i)); return out.toString() }
                        out.append(content, i, end + 1); i = end + 1
                    }
                    else -> {
                        val nextTag = content.indexOf('<', i)
                        val endOfText = if (nextTag < 0) content.length else nextTag
                        out.append(content.substring(i, endOfText).replace("'", "\\'"))
                        i = endOfText
                    }
                }
            }
            return out.toString()
        }
        val out = outDir.get().asFile
        out.deleteRecursively()
        out.mkdirs()
        srcDir.listFiles()?.filter { it.isDirectory }?.forEach { dir ->
            when {
                dir.name.startsWith("values") -> {
                    val targetDir = out.resolve(dir.name).apply { mkdirs() }
                    dir.listFiles()
                        ?.filter { it.isFile && it.extension == "xml" }
                        ?.forEach { xml ->
                            targetDir.resolve(xml.name).writeText(escape(xml.readText()))
                        }
                }
                dir.name.startsWith("drawable") -> {
                    val targetDir = out.resolve(dir.name).apply { mkdirs() }
                    dir.listFiles()
                        ?.filter { it.isFile }
                        ?.forEach { file ->
                            file.copyTo(targetDir.resolve(file.name), overwrite = true)
                        }
                }
            }
        }
    }
}

android.sourceSets.getByName("main").res.srcDir(composeResourcesMirrorDir.get().asFile)

tasks.named("preBuild") { dependsOn(mirrorComposeResourcesToAndroid) }
tasks.withType<Test>().configureEach { dependsOn(mirrorComposeResourcesToAndroid) }

tasks.withType<Test>().configureEach {
    val include = project.findProperty("coverageTestFilter") as String?
    val exclude = project.findProperty("coverageTestExclude") as String?
    if (include != null) filter { includeTestsMatching(include) }
    if (exclude != null) filter { excludeTestsMatching(exclude) }
}

kover {
    reports {
        filters {
            excludes {
                androidGeneratedClasses()
                classes(
                    "*.BuildConfig",
                    "*_Factory",
                    "*_Factory\$*",
                    "*_HiltModules*",
                    "*_Impl",
                    "*_MembersInjector",
                    "dagger.hilt.*",
                    "hilt_aggregated_deps.*",
                )
                annotatedBy(
                    "androidx.compose.ui.tooling.preview.Preview",
                    "androidx.compose.runtime.Composable",
                )
                packages(
                    "com.wojdor.memolki.di",
                    "com.wojdor.memolki.ui.component",
                    "com.wojdor.memolki.ui.theme",
                    "com.wojdor.memolki.ui.shape",
                    "com.wojdor.memolki.ui.ads",
                    "com.wojdor.memolki.ui.app",
                    "com.wojdor.memolki.util.notification",
                    "com.wojdor.memolki.util.media",
                    "com.wojdor.memolki.util.billing",
                    "com.wojdor.memolki.util.update",
                    "com.wojdor.memolki.util.gameservices",
                    "com.wojdor.memolki.data.crypto",
                    "com.wojdor.memolki.data.local.database",
                    "com.wojdor.memolki.util.provider",
                )
                classes(
                    "com.wojdor.memolki.ui.feature.*.component.*",
                    "*ScreenKt*",
                    "*Callbacks",
                    "ComposableSingletons*",
                    "*Dao_Impl",
                    "*_Impl",
                    "*Database_Impl",
                )
            }
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    coreLibraryDesugaring(libs.android.desugar)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime)
    implementation(libs.material)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.play.services.ads)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.mediation.unity)
    implementation(libs.mediation.ironsource)
    implementation(libs.mediation.liftoff)
    implementation(libs.mediation.inmobi)
    implementation(libs.mediation.mintegral)
    implementation(libs.billing)
    implementation(libs.review.ktx)
    implementation(libs.app.update.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.games)
    implementation(libs.kotlinx.coroutines.play.services)

    debugImplementation(libs.androidx.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)

    testImplementation(libs.paparazzi)
    testImplementation(libs.composable.preview.scanner)
    testImplementation(libs.test.parameter.injector)
}
