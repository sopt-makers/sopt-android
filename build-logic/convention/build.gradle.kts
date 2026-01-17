plugins {
    `kotlin-dsl`
}

group = "org.sopt.official.buildlogic"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.agp)
    compileOnly(libs.kotlin.gradleplugin)
    compileOnly(libs.compose.compiler.extension)
    implementation(libs.metro.plugin)
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
            id = "org.sopt.official.kotlin.android"
            implementationClass = "org.sopt.official.plugin.AndroidKotlinPlugin"
        }
        create("android-metro") {
            id = "org.sopt.official.metro"
            implementationClass = "org.sopt.official.plugin.AndroidMetroPlugin"
        }
        create("android-compose") {
            id = "org.sopt.official.compose"
            implementationClass = "org.sopt.official.plugin.AndroidComposePlugin"
        }
        create("android-test") {
            id = "org.sopt.official.test"
            implementationClass = "org.sopt.official.plugin.AndroidTestPlugin"
        }
        create("junit5") {
            id = "org.sopt.official.junit5"
            implementationClass = "org.sopt.official.plugin.JUnit5Plugin"
        }
        create("kotlin-serialization") {
            id = "org.sopt.official.serialization"
            implementationClass = "org.sopt.official.plugin.KotlinSerializationPlugin"
        }
        create("kotlin-jvm") {
            id = "org.sopt.official.kotlin.jvm"
            implementationClass = "org.sopt.official.plugin.KotlinJvmPlugin"
        }
        create("retrofit") {
            id = "org.sopt.official.retrofit"
            implementationClass = "org.sopt.official.plugin.RetrofitPlugin"
        }
        create("deeplink") {
            id = "org.sopt.official.deeplink"
            implementationClass = "org.sopt.official.plugin.DeeplinkPlugin"
        }
    }
}
