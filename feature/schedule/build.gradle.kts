plugins {
    sopt("feature")
    sopt("compose")
    sopt("deeplink")
}

android {
    namespace = "org.sopt.official.feature.schedule"
}

dependencies {
    // core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    // domain
    implementation(projects.domain.schedule)
}
