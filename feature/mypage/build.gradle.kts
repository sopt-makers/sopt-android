@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.sopt.official.feature")
}

android {
    namespace = "org.sopt.official.feature.mypage"
}

dependencies {

    implementation(projects.domain.soptamp)
    implementation(projects.domain.mypage)
    implementation(projects.core.common)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.timber)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)
    implementation(libs.rxbinding)

    implementation(platform(libs.firebase))
    implementation(libs.bundles.firebase)
    implementation(libs.process.phoenix)
}