import java.util.Properties

plugins {
    id("org.sopt.official.feature")
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.secret)
}

android {
    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    libraryVariants.all {
        sourceSets {
            getByName(name) {
                java.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    namespace = "org.sopt.official.stamp"
}

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlin.datetime)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.compose.destination.core)
    ksp(libs.compose.destination.ksp)
    implementation(libs.security)
    implementation(libs.bundles.accompanist)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    implementation(libs.timber)
    implementation(libs.coil.compose)
    implementation(libs.process.phoenix)
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.bundles.android.test)
    debugImplementation(libs.bundles.compose.android.test)
    debugImplementation(libs.bundles.flipper)
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
