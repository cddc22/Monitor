plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "github.leavesczy.monitor"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
//                groupId = "com.github.leavesCZY.Monitor"
//                artifactId = "monitor-no-op"
//                version = "1.0.0"
                from(components["release"])
            }
        }
    }
}