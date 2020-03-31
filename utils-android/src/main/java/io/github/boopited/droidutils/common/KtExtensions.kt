package io.github.boopited.droidutils.common

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true

fun Int?.orZero() = this ?: 0

fun Long?.orZero() = this ?: 0