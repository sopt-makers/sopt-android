plugins {
    sopt("kotlin.jvm")
}

kotlin {
    jvmToolchain(17)
}

dependencies{
    implementation(libs.javax.inject)
}
