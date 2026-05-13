plugins {
    sopt("feature")
    sopt("compose")
}

android {
    namespace = "org.sopt.official.sopletter"
}

dependencies {
    // core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.localstorage)
}