package com.boopited.utils_android.resource

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)