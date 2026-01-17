import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.secret) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.junit5) apply false
    alias(libs.plugins.app.distribution) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false
}

buildscript {
    dependencies {
        classpath(libs.junit5.android)
    }
}

subprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

    extensions.configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt")
            licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"))
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("${layout.buildDirectory}/**/*.kts")
            licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"), "(^(?![\\/ ]\\*).*$)")
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")
            licenseHeaderFile(rootProject.file("spotless/spotless.license.xml"), "(<[^!?])")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}