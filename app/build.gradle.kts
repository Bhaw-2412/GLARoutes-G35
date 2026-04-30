plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.gla.glaways"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gla.glaways"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // ✅ YE WALA BLOCK ADD KIYA HAI ERROR FIX KARNE KE LIYE
    packaging {
        resources {
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-spec.utf8"
        }
    }
}

dependencies {
    // UI & Support Libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")

    // Google Maps & Location
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:android-maps-utils:3.4.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Firebase
    implementation("com.google.firebase:firebase-database:20.3.1")

    // Image Zoom & Network
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.android.volley:volley:1.2.1")

    // Mail sent silently (Libraries added)
    implementation("com.sun.mail:android-activation:1.6.7")
    implementation("com.sun.mail:android-mail:1.6.7")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}