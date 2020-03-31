package io.github.boopited.droidutils.view

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

fun ImageView.tint(@ColorInt color: Int) {
    drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    invalidate()
}

fun View.tintBackground(@ColorInt tint: Int) {
    val drawableWrapper = DrawableCompat.wrap(background)
    DrawableCompat.setTint(drawableWrapper.mutate(), tint)
    background = drawableWrapper
}

private const val DISABLE_ALPHA = 0.4f
fun View.disable(disable: Boolean) {
    val disableAlpha = DISABLE_ALPHA
    val enableAlpha = 1.0f
    alpha = if (disable) disableAlpha else enableAlpha
}

fun TextView.fakeBold(bold: Boolean = true) {
    paint.isFakeBoldText = bold
}