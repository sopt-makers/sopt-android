plugins {
    sopt("feature")
    sopt("compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.secret)
}

android {
    namespace = "org.sopt.official.feature.fortune"
}

dependencies {
    // core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)

    // compose
    implementation(libs.kotlin.collections.immutable)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.compose.destination.core)
    ksp(libs.compose.destination.ksp)

    // etc
    implementation(libs.kotlin.datetime)
    implementation(libs.coil.compose)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.bundles.android.test)
    debugImplementation(libs.bundles.compose.android.test)
}
