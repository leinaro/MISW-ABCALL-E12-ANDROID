import java.util.Locale

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    id("com.google.devtools.ksp")
    alias(libs.plugins.screenshot)
    id("jacoco")
    alias(libs.plugins.googleGmsGoogleServices)
}

val exclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*"
)

android {
    namespace = "com.misw.abcall"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.misw.abcall"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    experimentalProperties["android.experimental.enableScreenshotTest"] = true

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    lint {
        baseline = file("lint-baseline.xml")
    }
    testFixtures  {
        enable = true
        androidResources = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    // Iterate over all application variants (e.g., debug, release)
    applicationVariants.all { variant ->
        // Extract variant name and capitalize the first letter
        val variantName = variant.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

        // Define task names for unit tests and Android tests
        val unitTests = "test${variantName}UnitTest"
        val androidTests = "connected${variantName}AndroidTest"

        // Register a JacocoReport task for code coverage analysis
        tasks.register<JacocoReport>("Jacoco${variantName}CodeCoverage") {
            // Depend on unit tests and Android tests tasks
            dependsOn(listOf(unitTests, androidTests))
            // Set task grouping and description
            group = "Reporting"
            description = "Execute UI and unit tests, generate and combine Jacoco coverage report"
            // Configure reports to generate both XML and HTML formats
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
            // Set source directories to the main source directory
            sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
            // Set class directories to compiled Java and Kotlin classes, excluding specified exclusions
            classDirectories.setFrom(files(
                fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                    exclude(exclusions)
                },
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(exclusions)
                }
            ))
            // Collect execution data from .exec and .ec files generated during test execution
            executionData.setFrom(files(
                fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
            ))
        }
        true
    }
}


tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-inappmessaging-display")
    implementation("com.google.firebase:firebase-analytics")

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
  //  implementation(libs.hilt.android)
   // kapt(libs.hilt.compiler)
    val hilt = "2.50"
    implementation("com.google.dagger:hilt-android:$hilt")
    ksp("com.google.dagger:hilt-compiler:$hilt")

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Images
    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)

    //
    implementation (libs.androidx.appcompat)

    implementation (libs.androidx.datastore.preferences)


    testImplementation(libs.junit)
 //   testImplementation(libs.hilt.android.testing)
 //   kaptTest(libs.hilt.android.compiler)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    screenshotTestImplementation(libs.androidx.ui.tooling)


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.uiautomator)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Required when asking for permission to post notifications (starting in Android 13)
   // implementation("androidx.activity:activity-ktx:1.9.3")
   // implementation("androidx.fragment:fragment-ktx:1.8.5")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging")

    implementation("com.google.firebase:firebase-installations:18.0.0")

    implementation("androidx.work:work-runtime:2.9.1")


}

// Allow references to generated code
/*kapt {
    correctErrorTypes = true
}*/