package com.boopited.utils_android.common

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true

fun Int?.orZero() = this ?: 0

fun Long?.orZero() = this ?: 0