/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
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
    alias(libs.plugins.app.distribution)
    alias(libs.plugins.kotlin.android)
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
            firebaseAppDistribution {
                artifactType = "APK"
                groups = "app-team"
            }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.domain.soptamp)
    implementation(projects.domain.mypage)
    implementation(projects.feature.soptamp)
    implementation(projects.data.soptamp)
    implementation(projects.data.mypage)
    implementation(projects.core.common)
    implementation(projects.core.analytics)
    implementation(projects.core.network)
    implementation(projects.core.auth)
    implementation(projects.core.authimpl)
    implementation(projects.core.webview)
    implementation(projects.feature.auth)
    implementation(projects.feature.mypage)
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

    implementation(libs.bundles.compose)

    implementation(libs.bundles.mavericks)

    implementation(platform(libs.firebase))
    implementation(libs.bundles.firebase)
    implementation(libs.sentry.compose)
    implementation(libs.process.phoenix)

    implementation(libs.compose.destination.core)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    ksp(libs.compose.destination.ksp)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.bundles.android.test)
    debugImplementation(libs.bundles.compose.android.test)

    implementation(libs.swiperefreshlayout)
    implementation(libs.coil.core)
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
