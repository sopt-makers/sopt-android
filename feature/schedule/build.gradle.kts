plugins {
    sopt("feature")
    sopt("compose")
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
