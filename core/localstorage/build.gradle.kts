plugins {
    sopt("feature")
}

android {
    namespace = "org.sopt.official.localstorage"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)

    implementation(libs.androidx.datastore.preferences)

    implementation(platform(libs.okhttp.bom))
}