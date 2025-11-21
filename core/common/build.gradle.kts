plugins {
    alias(libs.plugins.convention.cmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {

    sourceSets {

        commonMain.dependencies {

        }
        mobileMain.dependencies {

        }

        webCommonMain.dependencies {

        }


    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

}