plugins {
    sopt("feature")
}

android {
    namespace = "org.sopt.official.data.sopletter"
}

dependencies {
    // core
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)

    // domain
    implementation(projects.domain.sopletter)


}