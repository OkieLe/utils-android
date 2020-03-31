package io.github.boopited.droidutils.debug

import io.github.boopited.droidutils.BuildConfig

fun isDevMode(): Boolean {
    return arrayOf("debug").contains(BuildConfig.BUILD_TYPE)
}