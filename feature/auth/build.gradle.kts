@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
    id("org.sopt.official.serialization")
}

android {
    namespace = "org.sopt.official.auth"
}

dependencies {
    implementation(libs.customtab)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging.interceptor)
}
