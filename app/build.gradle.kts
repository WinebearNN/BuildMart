plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt") // добавляем плагин для ObjectBox
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.versaiilers.buildmart"
    compileSdk = 34

    viewBinding {
        enable = true
    }
    defaultConfig {
        applicationId = "com.versaiilers.buildmart"
        minSdk = 26
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {


//        // debugImplementation because LeakCanary should only run in debug builds.
//        debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.14")


    // Облегченная библиотека, содержит только карту, слой пробок,
    // LocationManager, UserLocationLayer
    // и возможность скачивать офлайн-карты (только в платной версии).
    implementation ("com.yandex.android:maps.mobile:4.9.0-lite")

    // Полная библиотека в дополнение к lite версии предоставляет автомобильную маршрутизацию,
    // веломаршрутизацию, пешеходную маршрутизацию и маршрутизацию на общественном транспорте,
    // поиск, suggest, геокодирование и отображение панорам.
    // implementation 'com.yandex.android:maps.mobile:4.9.0-full'



    // https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
// https://mvnrepository.com/artifact/com.github.bumptech.glide/annotations
    implementation("com.github.bumptech.glide:annotations:4.16.0")

    annotationProcessor( libs.compiler) // Или последнюю версию


    // https://mvnrepository.com/artifact/androidx.viewpager2/viewpager2
    runtimeOnly("androidx.viewpager2:viewpager2:1.0.0")



    implementation("androidx.core:core:1.12.0") // Use the latest stable version
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")


    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    val nav_version = "2.8.1"

    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


// https://mvnrepository.com/artifact/com.getkeepsafe.relinker/relinker
    implementation("com.getkeepsafe.relinker:relinker:1.4.5")


    implementation("io.objectbox:objectbox-android:3.6.0") // проверь последнюю версию
    implementation("io.objectbox:objectbox-kotlin:3.6.0") // последняя версия библиотеки
    kapt("io.objectbox:objectbox-processor:3.6.0") // для генерации кода, используем kapt

    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")

}
kapt {
    correctErrorTypes = true
}