import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.2.20"
}

kotlin {
    applyDefaultHierarchyTemplate()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    androidLibrary {
        namespace = "com.example.myktorapplication.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
//            jvmTarget = JvmTarget.JVM_11
            jvmTarget.set(JvmTarget.JVM_11) // Use .set() for modern Gradle property assignment
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation("io.ktor:ktor-client-okhttp:3.0.1")
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:3.0.1")
            }
        }
        commonMain.dependencies {
//            implementation(project(androidMain))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation.compose)
            //ktor
            implementation("io.ktor:ktor-client-core:3.0.1")
            implementation("io.ktor:ktor-client-content-negotiation:3.0.1")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.1")
            // Kotlinx Serialization JSON library
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
            // Coroutines (for managing asynchronous network calls)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
            // Core Coil compose dependency
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            // Network fetcher dependency (needed to load URLs via Ktor)
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.4")

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewModel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// This task is only required when building for iOS from Xcode.
// Disabling it for other builds prevents "Build cancelled" and configuration issues.
tasks.matching { it.name == "syncComposeResourcesForIos" }.configureEach {
    enabled = System.getenv("SDK_NAME") != null
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}