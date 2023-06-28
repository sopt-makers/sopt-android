plugins {
    `kotlin-dsl`
}

group = "org.sopt.official.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.agp)
    compileOnly(libs.kotlin.gradleplugin)
}

gradlePlugin {
    plugins {
        create("android-application") {
            id = "org.sopt.official.application"
            implementationClass = "org.sopt.official.plugin.AndroidApplicationPlugin"
        }
        create("android-feature") {
            id = "org.sopt.official.feature"
            implementationClass = "org.sopt.official.plugin.AndroidFeaturePlugin"
        }
        create("android-kotlin") {
            id = "org.sopt.official.kotlin"
            implementationClass = "org.sopt.official.plugin.AndroidKotlinPlugin"
        }
        create("android-hilt") {
            id = "org.sopt.official.hilt"
            implementationClass = "org.sopt.official.plugin.AndroidHiltPlugin"
        }
        create("kotlin-serialization") {
            id = "org.sopt.official.serialization"
            implementationClass = "org.sopt.official.plugin.KotlinSerializationPlugin"
        }
    }
}
