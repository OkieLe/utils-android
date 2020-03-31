package io.github.boopited.droidutils.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> Fragment.find(@IdRes id: Int): T = view?.findViewById(id) as T
inline fun <reified T : View> Dialog.find(@IdRes id: Int): T = findViewById(id)

inline fun <reified T : View> View.findOptional(@IdRes id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Activity.findOptional(@IdRes id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Fragment.findOptional(@IdRes id: Int): T? = view?.findViewById(id) as? T
inline fun <reified T : View> Dialog.findOptional(@IdRes id: Int): T? = findViewById(id) as? T

fun Context.inflate(@LayoutRes res: Int, parent: ViewGroup? = null, attach: Boolean = false): View {
    return LayoutInflater.from(this).inflate(res, parent, attach)
}