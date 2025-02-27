plugins {
    sopt("feature")
    sopt("compose")
}

android {
    namespace = "org.sopt.official.feature.main"
}

dependencies {
    implementation(projects.core.auth)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.webview)

    implementation(projects.feature.soptlog)
    implementation(projects.feature.home)
}
