import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("realm-android")
    id("kotlin-android-extensions")
}

val fis = FileInputStream(File(rootProject.rootDir, "local.properties"))
val prop = Properties()
prop.load(fis)

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "io.github.alxiw.openweatherforecast"
        minSdk = 21
        targetSdk = 31
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")

    // support
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.4.0")
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    // ui
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.github.lisawray.groupie:groupie:${Versions.groupie}")
    implementation("com.github.lisawray.groupie:groupie-kotlin-android-extensions:${Versions.groupie}")

    // di
    implementation("io.insert-koin:koin-core:${Versions.koin}")
    implementation("io.insert-koin:koin-androidx-viewmodel:${Versions.koin}")
    implementation("io.insert-koin:koin-android:${Versions.koin}")

    // rx
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxjava}")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // network
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}")

    // images
    implementation("com.github.bumptech.glide:glide:${Versions.glide}")
    kapt("com.github.bumptech.glide:glide:${Versions.glide}")

    // log
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.jakewharton.timber:timber:5.0.1")

    // debug
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")
    debugImplementation("com.facebook.stetho:stetho-okhttp3:1.6.0")

    // unit tests
    testImplementation("junit:junit:4.13.2")

    // ui tests
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
