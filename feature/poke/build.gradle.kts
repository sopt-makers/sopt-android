@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "org.sopt.official.feature.poke"
}

dependencies {
    implementation(projects.domain.poke)

    implementation(projects.core.auth)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(platform(libs.firebase))
    implementation(libs.bundles.firebase)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.swipe.refresh.layout)

    implementation(libs.coil.core)
    implementation(libs.timber)

    implementation(libs.android.lottie)
}