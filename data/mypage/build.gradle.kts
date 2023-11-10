@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
}

android {
    namespace = "org.sopt.official.data.mypage"
}

dependencies {
    implementation(projects.domain.mypage)
    implementation(projects.domain.soptamp)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.auth)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)

}