import java.time.LocalDateTime

fun getFallbackVersionName(): String {
    val now = LocalDateTime.now()
    return "${now.year}${now.monthValue}${now.dayOfYear}${now.hour}${now.minute}${now.second}-SNAPSHOT"
}

group = "com.arnyminerz.library"
version = System.getenv("GIT_COMMIT") ?: getFallbackVersionName()

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("io.gitlab.arturbosch.detekt") apply false
    id("com.codingfeline.buildkonfig") apply false
    id("org.jetbrains.dokka") apply false
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}
