package com.boopited.utils_android.view

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.ImageView
import androidx.annotation.ColorInt

fun ImageView.tint(@ColorInt color: Int) {
    drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    invalidate()
}