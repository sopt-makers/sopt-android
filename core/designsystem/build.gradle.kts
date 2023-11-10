@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
}

android {
    namespace = "org.sopt.official.designsystem"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(libs.material)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
}
