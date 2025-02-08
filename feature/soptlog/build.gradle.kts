plugins {
    sopt("feature")
    sopt("compose")
    sopt("test")
    sopt("deeplink")
}

android {
    namespace = "org.sopt.official.feature.soptlog"
}

ksp {
    arg("deepLink.incremental", "true")
    arg("deepLink.customAnnotations", "com.airbnb.AppDeepLink|com.airbnb.WebDeepLink")
}

dependencies {
    // core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    // domain
    implementation(projects.domain.soptlog)

    // etc
    implementation(libs.coil3.compose)
    implementation(libs.coil3.test)
}
