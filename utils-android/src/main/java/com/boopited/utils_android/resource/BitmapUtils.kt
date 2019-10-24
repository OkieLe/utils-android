package com.boopited.utils_android.resource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat

fun Context.bitmap(drawableId: Int): Bitmap {
    return when (val drawable = ContextCompat.getDrawable(this, drawableId)) {
        is BitmapDrawable -> BitmapFactory.decodeResource(resources, drawableId)
        is VectorDrawable -> drawable.bitmap()
        else -> throw IllegalArgumentException("unsupported drawable type")
    }
}

fun VectorDrawable.bitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        this.intrinsicWidth,
        this.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}