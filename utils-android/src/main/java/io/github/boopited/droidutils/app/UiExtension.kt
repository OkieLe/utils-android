package io.github.boopited.droidutils.app

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.view.View
import android.view.WindowManager
import android.os.Build
import android.annotation.TargetApi
import android.graphics.Color
import androidx.annotation.ColorInt

fun Activity.startActivitySafely(intent: Intent) {
    try {
        this.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
    }
}

fun Context.hideKeyboard(view: View) {
    val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    input.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(force: Boolean) {
    val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    input.toggleSoftInput(
        if (force) InputMethodManager.SHOW_FORCED else InputMethodManager.SHOW_IMPLICIT,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.drawUnderStatusBar(@ColorInt color: Int = Color.TRANSPARENT) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    )
    window.statusBarColor = color
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.drawBelowStatusBar(@ColorInt color: Int = Color.LTGRAY) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
    )
    window.statusBarColor = color
}

fun Activity.setStatusBarTheme(light: Boolean = false) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (light) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_VISIBLE
            }
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.statusBarColor = Color.LTGRAY
        }
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}