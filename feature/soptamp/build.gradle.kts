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
    implementation(projects.domain.soptamp)
    implementation(projects.core.common)
    implementation(projects.core.analytics)
    implementation(libs.kotlin.datetime)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.compose.destination.core)
    ksp(libs.compose.destination.ksp)
    implementation(libs.bundles.accompanist)
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.coil.compose)
    implementation(libs.process.phoenix)
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.bundles.android.test)
    debugImplementation(libs.bundles.compose.android.test)
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
