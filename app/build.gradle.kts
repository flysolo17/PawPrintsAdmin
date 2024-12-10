plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)


        id("kotlin-parcelize")

        id("kotlin-kapt")
        id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.eutech.pawprints"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.eutech.pawprints"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

        //for calendar

       // isCoreLibraryDesugaringEnabled = true

    }
    kotlinOptions {
        jvmTarget = "1.8"

    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.play.services.analytics.impl)
    //calendar
    coreLibraryDesugaring(" 'com.android.tools:desugar_jdk_libs:2.0.3'")
    implementation("com.kizitonwose.calendar:compose:2.5.4")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //coil for image rendering
    implementation("io.coil-kt:coil-compose:2.6.0")

    //courotines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")


    //Google fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.7")
    //navagation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    //viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    //hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.48")


    //window size
    implementation ("androidx.compose.material3:material3-window-size-class:1.2.1")


    //for color picker
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.2.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:color:1.2.0")
    //icons
    implementation("androidx.compose.material:material-icons-extended:1.5.3")

    implementation ("androidx.compose.foundation:foundation:1.7.4")




    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Number picker
    implementation ("com.chargemap.compose:numberpicker:1.0.3")




    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")
}

kapt {
    correctErrorTypes = true
}