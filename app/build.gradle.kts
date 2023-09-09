import java.util.*

plugins {
    id("org.sopt.official.application")
    id("org.sopt.official.test")
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secret)
    alias(libs.plugins.sentry)
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
android {
    namespace = "org.sopt.official"

    defaultConfig {
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()
        applicationId = "org.sopt.official"

        if (project.hasProperty("dev")) {
            versionNameSuffix = "-Dev"
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = File("${project.rootDir.absolutePath}/keystore/debug.keystore")
            storePassword = "android"
        }
        create("release") {
            keyAlias = properties.getProperty("keyAlias")
            keyPassword = properties.getProperty("keyPassword")
            storeFile = File("${project.rootDir.absolutePath}/keystore/releaseKey.jks")
            storePassword = "soptandroid"
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
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
    applicationVariants.all {
        val variant = this
        kotlin {
            sourceSets {
                getByName(variant.name) {
                    kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
                }
            }
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(projects.feature.soptamp)
    implementation(projects.core)
    implementation(projects.feature.auth)
    implementation(libs.kotlin.coroutines.google.play)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.startup)
    implementation(libs.security)

    implementation(libs.bundles.accompanist)
    implementation(libs.inappupdate)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    implementation(libs.timber)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)
    implementation(libs.rxbinding)

    implementation(libs.bundles.compose)

    implementation(libs.bundles.mavericks)

    implementation(platform(libs.firebase))
    implementation(libs.bundles.firebase)
    implementation(libs.sentry.compose)
    implementation(libs.process.phoenix)

    implementation(libs.compose.destination.core)
    ksp(libs.compose.destination.ksp)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.bundles.android.test)
    debugImplementation(libs.bundles.compose.android.test)

    debugImplementation(libs.bundles.flipper)
    releaseImplementation(libs.flipper.noop)
    debugImplementation(libs.flipper.network) {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"

    ignoreList.add("sdk.*")
}

ktlint {
    android.set(true)
    debug.set(true)
    coloredOutput.set(true)
    verbose.set(true)
    outputToConsole.set(true)
    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
        exclude { it.file.name.contains("gradle") }
    }
}
