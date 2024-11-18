plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.room") version "2.6.1" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12"
}

android {
    namespace = "com.example.dognutritionapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dognutritionapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding=true
    }
}

dependencies {
    implementation(libs.filament.android)
    val roomVersion = "2.6.1"
    val lifecycleVersion = "2.8.5"
    val coroutineVersion = "1.3.9"
    val materialUIv190 = "1.9.0"
    val lottieVersion = "3.4.0"



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Room Dependencies
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Lifecycle dependencies
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Coroutines Dependencies
    implementation(libs.kotlinx.coroutines.android)

    implementation (libs.glide)
    implementation(libs.dotsindicator)
    implementation(libs.material.v190) // implementation("com.google.android.material:material:1.9.0")

    //Custom Toast
    implementation ("com.github.Spikeysanju:MotionToast:1.4")
    implementation ("com.airbnb.android:lottie:$lottieVersion")
}