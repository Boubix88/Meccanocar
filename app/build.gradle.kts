import java.io.BufferedReader
import java.io.InputStreamReader

plugins {
    id("com.android.application")
}

fun getVersionCode(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--all", "--count").start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val code = reader.readLine()?.trim()?.toInt() ?: 1
        process.waitFor()
        code
    } catch (ignored: Exception) {
        1
    }
}

fun getVersionName(): String {
    return try {
        val process = ProcessBuilder("git", "describe", "--tags", "--dirty").start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val name = reader.readLine()?.trim() ?: "1.0.0"
        process.waitFor()
        name
    } catch (ignored: Exception) {
        "1.0.0"
    }
}

android {
    namespace = "com.example.meccanocar"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.meccanocar"
        minSdk = 26
        targetSdk = 33

        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.3.0-alpha02")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
    implementation("commons-io:commons-io:2.15.1")
}