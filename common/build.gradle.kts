import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("io.gitlab.arturbosch.detekt")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.codingfeline.buildkonfig")
    `maven-publish`
}

val kotlinVersion: String = project.property("kotlin.version") as String
val mokoResourcesVersion: String = project.property("moko-resources.version") as String

val androidCompileSdk: Int = (project.property("android.compileSdk") as String).toInt()
val androidMinSdk: Int = (project.property("android.minSdk") as String).toInt()

group = "com.arnyminerz.library.kmconnector"
version = System.getenv("GIT_COMMIT") ?: "development"

kotlin {
    android()

    jvm("desktop") {
        jvmToolchain(17)
    }

    @Suppress("UnusedPrivateProperty")
    sourceSets {
        all {
            languageSettings.enableLanguageFeature("ContextReceivers")
        }

        val commonMain by getting {
            multiplatformResources {
                multiplatformResourcesPackage = "com.arnyminerz.library.kmconnector"
            }

            buildkonfig {
                packageName = "com.arnyminerz.library.kmconnector"

                // default config is required
                defaultConfigs {
                    buildConfigField(STRING, "PreferencesContainer", "KM-Connector")
                }
            }

            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.materialIconsExtended)

                implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

                api("dev.icerock.moko:resources:$mokoResourcesVersion")
                api("dev.icerock.moko:resources-compose:$mokoResourcesVersion")

                api("org.json:json:20230618")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("dev.icerock.moko:resources-test:$mokoResourcesVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.compose.material3:material3-window-size-class:1.1.1")
                api("androidx.core:core-ktx:1.10.1")
                api("androidx.datastore:datastore-preferences:1.0.0")
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
                api("androidx.compose.runtime:runtime-livedata:1.4.3")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
        val desktopTest by getting
    }
}

tasks.withType<Detekt>().configureEach {
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

android {
    compileSdk = androidCompileSdk

    namespace = "com.arnyminerz.library.kmconnector"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = androidMinSdk
    }

    kotlin {
        jvmToolchain(17)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        group = "com.arnyminerz.library.kmconnector"
        version = System.getenv("GIT_COMMIT") ?: "development"

        artifact(javadocJar.get())

        pom {
            name.set("KM Connector")
            description.set(
                "A library that provides a way to have a common interface that connects different platforms."
            )
            url.set("https://github.com/ArnyminerZ/KM-Connector")
            developers {
                developer {
                    id.set("arnyminerz")
                    name.set("Arnau Mora")
                    email.set("arnyminerz@proton.me")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/ArnyminerZ/KM-Connector.git")
                developerConnection.set("scm:git:ssh://github.com/ArnyminerZ/KM-Connector.git")
                url.set("https://github.com/ArnyminerZ/KM-Connector")
            }
        }
    }
}
