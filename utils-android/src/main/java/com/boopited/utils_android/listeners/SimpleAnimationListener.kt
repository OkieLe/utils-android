package com.boopited.utils_android.listeners

import android.view.animation.Animation

interface SimpleAnimationListener: Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
        // Extender only need override necessary method
    }

    override fun onAnimationEnd(animation: Animation?) {
        // Extender only need override necessary method
    }

    override fun onAnimationStart(animation: Animation?) {
        // Extender only need override necessary method
    }

}