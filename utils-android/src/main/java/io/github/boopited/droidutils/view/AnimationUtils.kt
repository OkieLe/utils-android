package io.github.boopited.droidutils.view

import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

const val DEFAULT_ANIMATION_DURATION = 120L

fun View.fadeIn(parent: ViewGroup, duration: Long = DEFAULT_ANIMATION_DURATION) {
    val transition = Fade().also {
        it.duration = duration
        it.mode = Fade.MODE_IN
        it.addTarget(this)
    }
    TransitionManager.beginDelayedTransition(parent, transition)
    visibility = View.VISIBLE
}

fun View.fadeOut(parent: ViewGroup, duration: Long = DEFAULT_ANIMATION_DURATION) {
    val transition = Fade().also {
        it.duration = duration
        it.mode = Fade.MODE_OUT
        it.addTarget(this)
    }
    TransitionManager.beginDelayedTransition(parent, transition)
    visibility = View.GONE
}

fun ViewGroup.fadeInViews(vararg views: View, duration: Long = DEFAULT_ANIMATION_DURATION) {
    val transition = Fade().also { fade ->
        fade.duration = duration
        fade.mode = Fade.MODE_IN
        views.forEach { fade.addTarget(it) }
    }
    TransitionManager.beginDelayedTransition(this, transition)
    views.forEach { it.visibility = View.VISIBLE }
}

fun ViewGroup.fadeOutViews(vararg views: View, duration: Long = DEFAULT_ANIMATION_DURATION) {
    val transition = Fade().also { fade ->
        fade.duration = duration
        fade.mode = Fade.MODE_OUT
        views.forEach { fade.addTarget(it) }
    }
    TransitionManager.beginDelayedTransition(this, transition)
    views.forEach { it.visibility = View.GONE }
}

fun View.slideIn(parent: ViewGroup, edge: Int = Gravity.START, duration: Long = DEFAULT_ANIMATION_DURATION) {
    val transition = TransitionSet().also {
        it.addTransition(
            Slide(edge).apply { mode = Slide.MODE_IN }
        )
        it.addTransition(
            Fade().apply { mode = Fade.MODE_IN }
        )
        it.duration = duration
        it.addTarget(this)
    }
    TransitionManager.beginDelayedTransition(parent, transition)
    visibility = View.VISIBLE
}

fun View.slideOut(parent: ViewGroup, edge: Int = Gravity.START, duration: Long = DEFAULT_ANIMATION_DURATION) {
    val transition = TransitionSet().also {
        it.addTransition(
            Slide(edge).apply { mode = Slide.MODE_OUT }
        )
        it.addTransition(
            Fade().apply { mode = Fade.MODE_OUT }
        )
        it.duration = duration
        it.addTarget(this)
    }
    TransitionManager.beginDelayedTransition(parent, transition)
    visibility = View.GONE
}