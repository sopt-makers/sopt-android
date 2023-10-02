@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
}

android {
    namespace = "org.sopt.official.analytics"
}

dependencies {
    implementation(libs.amplitude.android)
    implementation(libs.timber)
}
