plugins {
    sopt("feature")
}

android {
    namespace = "org.sopt.official.data.auth"
}

dependencies {
    implementation(projects.domain.auth)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
}