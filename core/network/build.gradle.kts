@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
}

android {
    namespace = "org.sopt.official.network"

}

dependencies {
    implementation(projects.core.common)
    implementation(libs.security)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.process.phoenix)
    implementation(libs.timber)
    debugImplementation(libs.bundles.flipper)
    releaseImplementation(libs.flipper.noop)
    debugImplementation(libs.flipper.network) {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }
}