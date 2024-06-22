import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    id ("kotlin-android")
    id ("androidx.navigation.safeargs")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {

    namespace = "com.example.quikcartadmin"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.example.quikcartadmin"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties().apply {
            load(FileInputStream(rootProject.file("local.properties")))
        }
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
        buildConfigField("String", "HOSTNAME", "\"${properties.getProperty("HOSTNAME")}\"")
        buildConfigField("String", "PASSWORD", "\"${properties.getProperty("PASSWORD")}\"")
        buildConfigField("String", "VERSION", "\"${properties.getProperty("VERSION")}\"")
        buildConfigField("String", "API_Secret_Key ", "\"${properties.getProperty("API_Secret_Key")}\"")

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
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.runner)
    testImplementation(project(":app"))
    testImplementation(project(":app"))
    val coroutinesVersion = ("1.5.0")
    val androidXTestCoreVersion = ("1.4.0")
    val androidXTestExtKotlinRunnerVersion = ("1.1.3")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Room
    implementation ("androidx.room:room-ktx:2.5.0")
    implementation ("androidx.room:room-runtime:2.5.0")
    kapt ("androidx.room:room-compiler:2.5.0")
    // Navigation Components
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    //cardView
    implementation ("androidx.cardview:cardview:1.0.0")
    //Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    //Room
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    //ViewModel & livedata
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    //Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.activity:activity-ktx:1.8.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    //refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // kotlinx-coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    //glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    // Glide v4 uses this new annotation processor -- see https://bumptech.github.io/glide/doc/generatedapi.html
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //GSON
    implementation ("com.google.code.gson:gson:2.10.1")
    //rounded images
    implementation ("com.makeramen:roundedimageview:2.3.0")
    //animation
    implementation ("com.airbnb.android:lottie:3.4.0")
    //circle image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //map
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    // Hilt
    implementation ("com.google.dagger:hilt-android:2.48.1")
    kapt ("com.google.dagger:hilt-compiler:2.48.1")
    //curved bottom nav
    implementation ("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-storage")
    //picasso
    implementation ("com.squareup.picasso:picasso:2.8")
    // For local unit tests
    testImplementation ("com.google.dagger:hilt-android-testing:2.48")
    kaptTest ("com.google.dagger:hilt-compiler:2.48")
    // hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")
    // AndroidX and Robolectric
    testImplementation("androidx.test.ext:junit-ktx:$androidXTestExtKotlinRunnerVersion")
    testImplementation("androidx.test:core-ktx:$androidXTestCoreVersion")
    testImplementation("org.robolectric:robolectric:4.8")
    // InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    // Dependencies for local unit tests
    testImplementation(libs.junit)
    testImplementation(libs.hamcrest.all)
    testImplementation(libs.androidx.arch.core.core.testing)
    testImplementation(libs.robolectric.robolectric)
    // AndroidX Test - JVM testing
    testImplementation(libs.test.core.ktx)
    // AndroidX Test - Instrumented testing
    androidTestImplementation(libs.androidx.junit.v113)
    androidTestImplementation(libs.androidx.espresso.core.v340)
}

kapt{
    correctErrorTypes = true
}