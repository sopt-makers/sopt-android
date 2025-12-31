plugins {
    sopt("feature")
    sopt("test")
}

android {
    namespace = "org.sopt.official.data.appjamtamp"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.domain.appjamtamp)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
}
