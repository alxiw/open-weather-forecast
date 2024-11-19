import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("realm-android")
}

val fis = FileInputStream(File(rootProject.rootDir, "local.properties"))
val prop = Properties()
prop.load(fis)

android {

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
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    buildFeatures {
        viewBinding = true
    }
    namespace = "io.github.alxiw.openweatherforecast"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.21")

    // support
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.8.7")
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // ui
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.github.lisawray.groupie:groupie:2.10.1")
    implementation("com.github.lisawray.groupie:groupie-viewbinding:2.10.1")

    // di
    implementation("io.insert-koin:koin-core:2.0.1")
    implementation("io.insert-koin:koin-androidx-viewmodel:2.0.1")
    implementation("io.insert-koin:koin-android:2.0.1")

    // rx
    implementation("io.reactivex.rxjava3:rxjava:3.1.6")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    // network
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

    // images
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:glide:4.16.0")

    // log
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.jakewharton.timber:timber:5.0.1")

    // debug
    //debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")

    // unit tests
    testImplementation("junit:junit:4.13.2")

    // ui tests
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
