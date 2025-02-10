plugins {
    sopt("feature")
}

android {
    namespace = "org.sopt.official.data.soptlog"
}

dependencies {
    implementation(projects.domain.soptlog)

    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
}
