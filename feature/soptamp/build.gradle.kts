/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
