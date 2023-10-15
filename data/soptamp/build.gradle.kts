@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
}

android {
    namespace = "org.sopt.official.data.soptamp"
}

dependencies {
    implementation(projects.domain.soptamp)
    implementation(projects.core.common)
    implementation(libs.security)
    implementation(libs.timber)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    implementation(libs.process.phoenix)
}