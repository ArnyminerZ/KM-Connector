import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.dokka.gradle.DokkaTask
import io.gitlab.arturbosch.detekt.Detekt
import java.time.LocalDateTime
import java.util.Properties

fun getFallbackVersionName(): String {
    val now = LocalDateTime.now()
    return "${now.year}${now.monthValue}${now.dayOfYear}${now.hour}${now.minute}${now.second}-SNAPSHOT"
}

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("io.gitlab.arturbosch.detekt")
    id("com.codingfeline.buildkonfig")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

val kotlinVersion: String = project.property("kotlin.version") as String
val mokoResourcesVersion: String = project.property("moko-resources.version") as String

val androidCompileSdk: Int = (project.property("android.compileSdk") as String).toInt()
val androidMinSdk: Int = (project.property("android.minSdk") as String).toInt()

group = "com.arnyminerz.library.kmconnector"
version = System.getenv("GIT_COMMIT") ?: getFallbackVersionName()

kotlin {
    android {
        publishAllLibraryVariants()
    }

    jvm("desktop") {
        jvmToolchain(17)
    }

    @Suppress("UnusedPrivateProperty")
    sourceSets {
        all {
            languageSettings.enableLanguageFeature("ContextReceivers")
        }

        val commonMain by getting {
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

val dokkaOutputDir = "$buildDir/dokka"

tasks.named("dokkaHtml", DokkaTask::class) {
    outputDirectory.set(file(dokkaOutputDir))
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}

fun MavenPublication.setPomInformation() {
    pom {
        name.set("KM Connector")
        description.set(
            "A library that provides a way to have a common interface that connects different platforms."
        )
        url.set("https://github.com/ArnyminerZ/KM-Connector")
        issueManagement {
            system.set("Github")
            url.set("https://github.com/Kodein-Framework/Kodein-DI/issues")
        }
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

val localProperties = Properties().apply {
    load(File(rootDir, "local.properties").inputStream())
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                name = "oss"
                val repoId = localProperties.getProperty("SONATYPE_REPOSITORY_ID")
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/$repoId/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                credentials {
                    username = localProperties.getProperty("SONATYPE_USERNAME")
                    password = localProperties.getProperty("SONATYPE_PASSWORD")
                }
            }
        }
        publications.create("release", MavenPublication::class.java) {
            group = "com.arnyminerz.library.kmconnector"
            version = System.getenv("GIT_COMMIT") ?: getFallbackVersionName()

            artifact(javadocJar.get())

            setPomInformation()
        }
    }
    signing {
        useGpgCmd()
        sign(publishing.publications["release"])
    }
}
