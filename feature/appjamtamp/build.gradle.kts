plugins {
    sopt("feature")
    sopt("compose")
    sopt("test")
}

android {
    namespace = "org.sopt.official.feature.appjamtamp"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)

    implementation(libs.kotlin.collections.immutable)
}
