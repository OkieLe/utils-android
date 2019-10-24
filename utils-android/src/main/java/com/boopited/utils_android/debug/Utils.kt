package com.boopited.utils_android.debug

import com.boopited.utils_android.BuildConfig

fun isDevMode(): Boolean {
    return arrayOf("debug").contains(BuildConfig.BUILD_TYPE)
}