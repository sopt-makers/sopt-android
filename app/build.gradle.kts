plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secret)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "org.sopt.official"
    compileSdk = 33

    defaultConfig {
        applicationId = "org.sopt.official"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(libs.kotlin.serialization.json)
    implementation(libs.core.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.startup)
    implementation(libs.security)

    implementation(libs.material)
    implementation(libs.bundles.accompanist)
    implementation(libs.inappupdate)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    implementation(libs.timber)

    implementation(libs.bundles.compose)

    implementation(libs.bundles.mavericks)

    implementation(libs.hilt)
    kapt(libs.hilt.kapt)

    implementation(platform(libs.firebase))
    implementation(libs.bundles.firebase)

    implementation(libs.compose.destination.core)
    ksp(libs.compose.destination.ksp)

    androidTestImplementation(platform(libs.compose.bom))
    testImplementation(libs.junit)
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
}