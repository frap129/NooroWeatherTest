import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val weatherApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("weatherApiKey")

plugins {
    alias(libs.plugins.kotlinSymbolProcessor)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "2.1.0"
}

android {
    namespace = "core.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("String", "weatherApiKey", weatherApiKey)
        }
        release {
            buildConfigField("String", "weatherApiKey", weatherApiKey)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.util.timber)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.androidx.ext.junit)
    androidTestImplementation(libs.test.espresso.core)
}
