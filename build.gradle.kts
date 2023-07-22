group = "com.arnyminerz.library"
version = System.getenv("GIT_COMMIT")

allprojects {
    repositories {
        google()
        mavenCentral()
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
    id("dev.icerock.mobile.multiplatform-resources") apply false
    id("com.codingfeline.buildkonfig") apply false
}
