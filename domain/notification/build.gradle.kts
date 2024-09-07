plugins {
    sopt("kotlin")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.javax.inject)
}