plugins {
    sopt("feature")
}

android {
    namespace = "org.sopt.official.data.schedule"
}

dependencies {
    implementation(projects.domain.schedule)

    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
}
