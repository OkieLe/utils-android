package com.boopited.utils_android.resource

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.dimenPixel(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Context.dimenFloat(@DimenRes id: Int) = resources.getDimension(id)