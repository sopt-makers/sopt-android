plugins {
    sopt("feature")
}

android {
    namespace = "org.sopt.official.data.home"
}

dependencies {
    implementation(projects.domain.home)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
}
