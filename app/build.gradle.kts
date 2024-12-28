import java.util.Locale

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    jacoco
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.musfick.albumfetcher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.musfick.albumfetcher"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.musfick.albumfetcher.core.utils.CustomTestRunner"
    }

    buildTypes {
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.rules)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)


    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)


    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //Logcat
    implementation(libs.logcat)

    //Chucker
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)


    //Testing Related Dep
    testImplementation(libs.turbine)
    androidTestImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    androidTestImplementation(libs.mockwebserver)
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    testImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.androidx.core)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}

kapt {
    correctErrorTypes = true
}

val exclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "**/*Composable*",
    "**/ComposableSingletons*",
    "**/tmp/**/*.class",
    "**/*\$*",
    "**/di/**",
    "**/mapper/**",
    "**/dto/**",
    "**/entity/**",
    "**/model/**",
    "**/ui/theme/**",
    "**/*_*",
    "**/*Preview*",
    "**/hilt_aggregated_deps",
    "**/dagger",
    "**/test",
    "**/utils/**",
    "**/BaseApplication.class",
    "**/AppDatabase.class",
)

android {
    applicationVariants.all(closureOf<com.android.build.gradle.internal.api.BaseVariantImpl> {
        val variant = this@closureOf.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        val unitTests = "test${variant}UnitTest"
        val androidTests = "connected${variant}AndroidTest"

        tasks.register<JacocoReport>("Jacoco${variant}CodeCoverage") {
            dependsOn(listOf(unitTests, androidTests))
            group = "Reporting"
            description = "Execute ui and unit tests, generate and combine Jacoco coverage report"
            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(true)
            }
            sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
            classDirectories.setFrom(files(
                fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                    exclude(exclusions)
                },
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(exclusions)
                }
            ))
            executionData.setFrom(files(
                fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
            ))
        }
    })
}
