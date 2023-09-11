plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "io.drdroid.paypalincl"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.drdroid.paypalincl"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.ui:ui-graphics:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.compose.material:material-icons-extended:1.6.0-alpha05")
    implementation("androidx.appcompat:appcompat:1.6.1")

    //Facebook Login
    implementation("com.facebook.android:facebook-login:12.2.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    //hilt
    implementation("com.google.dagger:hilt-android:2.44.2")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    kapt("com.google.dagger:hilt-compiler:2.44.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")

    //Gson
    implementation("com.google.code.gson:gson:2.10.1")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.7.2")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.1")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")

    //System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    //Pager Indicator
//    implementation("com.google.accompanist:accompanist-pager-indicators:0.27.1")
    //Paging Compose
    implementation("com.google.accompanist:accompanist-pager:0.13.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.13.0")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    //Permission
    implementation("com.google.accompanist:accompanist-permissions:0.25.0")

    //Landscapist
    implementation("com.github.skydoves:landscapist-coil:2.2.8")
    implementation("com.github.skydoves:landscapist-placeholder:2.2.8")
    implementation("com.github.skydoves:landscapist-palette:2.2.8")

    //Paypal SDK
    implementation("com.paypal.checkout:android-sdk:1.1.0")
    implementation("com.paypal.sdk:paypal-android-sdk:2.14.2")
    implementation("com.paypal.android:card-payments:0.0.13")
    implementation("com.paypal.android:paypal-web-payments:0.0.13")
    implementation("com.paypal.android:card-payments:0.0.14-SNAPSHOT")
    implementation("com.paypal.android:payment-buttons:0.0.14-SNAPSHOT")
//    implementation("com.paypal.android:paypal-native-payments:0.0.13")

    //SSJetPackComposeProgressButton
    implementation("com.github.SimformSolutionsPvtLtd:SSJetPackComposeProgressButton:1.0.7")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}