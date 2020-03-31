package io.github.boopited.droidutils.app

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

inline fun <reified T> AppCompatActivity.findFragmentById(@IdRes id: Int): T {
    return supportFragmentManager.findFragmentById(id) as T
}

inline fun <reified T> AppCompatActivity.findFragmentByTag(tag: String): T? {
    return supportFragmentManager.findFragmentByTag(tag) as T?
}

fun AppCompatActivity.showFragmentWithId(
    fragment: Fragment, @IdRes frameId: Int = 0,
    addToBackStack: Boolean = false,
    vararg toHideFragments: Fragment? = emptyArray()
) {
    val transaction = supportFragmentManager.beginTransaction()
    if (frameId != 0 && !fragment.isAdded) {
        transaction.add(frameId, fragment)
    }
    transaction.show(fragment)
    toHideFragments
        .filterNotNull()
        .forEach { transaction.hide(it) }
    if (addToBackStack) {
        transaction.addToBackStack(null)
    }
    transaction.commitAllowingStateLoss()
}

fun AppCompatActivity.showFragmentWithTag(
    fragment: Fragment, tag: String? = null,
    addToBackStack: Boolean = false,
    vararg toHideFragments: Fragment? = emptyArray()
) {
    val transaction = supportFragmentManager.beginTransaction()
    if (!tag.isNullOrBlank() && !fragment.isAdded) {
        transaction.add(fragment, tag)
    }
    transaction.show(fragment)
    toHideFragments
        .filterNotNull()
        .forEach { transaction.hide(it) }
    if (addToBackStack) {
        transaction.addToBackStack(null)
    }
    transaction.commitAllowingStateLoss()
}

fun AppCompatActivity.hideFragmentsExcept(shownTag: String) {
    val ft = supportFragmentManager.beginTransaction()
    supportFragmentManager.fragments.forEach {
        if (it.tag != shownTag) {
            ft.hide(it)
        }
    }
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.hideFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .hide(fragment)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .remove(fragment)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment, @IdRes frameId: Int,
    tag: String? = null,
    addToBackStack: Boolean = false
) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(frameId, fragment, tag)
    if (addToBackStack) {
        transaction.addToBackStack(null)
    }
    transaction.commitAllowingStateLoss()
}

/**
 * android:fitsSystemWindows not working when replacing fragments
 *
 * When the content is being changed, system will no longer dispatch those insets to the view hierarchy.
 * This can be easily solved with ViewCompat.requestApplyInsets(View) API.
 * That would force the system to dispatch WindowInsets once again.
 */
fun AppCompatActivity.replaceFragmentNow(
    fragment: Fragment, @IdRes frameId: Int, tag: String? = null
) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(frameId, fragment, tag)
    // NOTE, we are performing `commitNow()` instead of ordinary `commit()`,
    // because we want this commit to happen synchronously/immediately.
    transaction.commitNowAllowingStateLoss()
    // Ask the framework to dispatch window insets once more to the root of your view hierarchy
    ViewCompat.requestApplyInsets(findViewById(frameId))
}