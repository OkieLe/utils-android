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

fun Context.resourceIdFromString(resourceIdString: String?): Int {
    var idString: String = resourceIdString ?: return 0

    if (idString.startsWith("@")) {
        idString = idString.substring(1)
    }

    val pair =
        idString
            .split("/".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
    require(pair.size == 2) { "Resource parameter is malformed: $resourceIdString" }

    return resources.getIdentifier(pair[1], pair[0], packageName)
}