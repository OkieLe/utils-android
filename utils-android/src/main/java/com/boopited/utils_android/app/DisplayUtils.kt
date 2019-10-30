package com.boopited.utils_android.app

import android.app.Activity
import android.content.Context
import android.graphics.Point

fun Context.px2dp(pxValue: Float): Float {
    val scale = resources.displayMetrics.density
    return pxValue / scale + 0.5f
}

fun Context.dp2px(dipValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun Context.sp2px(sp: Float): Float {
    val scale = resources.displayMetrics.scaledDensity
    return sp * scale + 0.5f
}

fun Context.statusBarHeight(): Int {
    val resourceId = resources
        .getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        dp2px(24f)
    }
}

fun Context.navigationBarHeight(): Int {
    val resourceId = resources
        .getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun getScreenWidth(activity: Activity): Int {
    val display = activity.windowManager.defaultDisplay
    val point = Point()
    if (display != null) {
        display.getSize(point)
        return point.x
    }
    return 0
}

fun getScreenHeight(activity: Activity): Int {
    val display = activity.windowManager.defaultDisplay
    val point = Point()
    if (display != null) {
        display.getSize(point)
        return point.y - activity.statusBarHeight()
    }
    return 0
}
