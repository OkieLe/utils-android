package io.github.boopited.droidutils.listeners

import android.animation.Animator

interface SimpleAnimatorListener: Animator.AnimatorListener {
    override fun onAnimationRepeat(animator: Animator) {
        // Extender only need override necessary method
    }

    override fun onAnimationEnd(animator: Animator) {
        // Extender only need override necessary method
    }

    override fun onAnimationCancel(animator: Animator) {
        // Extender only need override necessary method
    }

    override fun onAnimationStart(animator: Animator) {
        // Extender only need override necessary method
    }
}