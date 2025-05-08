plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
    id("publish-conventions")
    id("targets-conventions")
}

kotlin {
//    explicitApi()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        all {
            languageSettings.enableLanguageFeature("PropertyParamAnnotationDefaultTargetMode")
        }
    }

}