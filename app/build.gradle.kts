import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.realm.kotlin)
}

val fis = FileInputStream(File(rootProject.rootDir, "local.properties"))
val prop = Properties()
prop.load(fis)

android {
    namespace = "io.github.alxiw.openweatherforecast"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.alxiw.openweatherforecast"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", prop["apikey"] as String)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.kotlin.stdlib.jdk8)

    // support
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.work.runtime.ktx)

    // ui
    implementation(libs.material)
    implementation(libs.groupie)
    implementation(libs.groupie.viewbinding)

    // di
    implementation(libs.koin.android)

    // rx
    implementation(libs.coroutines.android)

    // network
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // images
    implementation(libs.coil)

    // db
    implementation(libs.realm.kotlin)

    // log
    implementation(libs.logging.interceptor)

    // unit tests
    testImplementation(libs.junit)

    // ui tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
