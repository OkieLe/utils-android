package com.boopited.utils_android.listeners

import android.transition.Transition

interface SimpleTransitionListener: Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition) {
        // Extender only need override necessary method
    }

    override fun onTransitionResume(transition: Transition) {
        // Extender only need override necessary method
    }

    override fun onTransitionPause(transition: Transition) {
        // Extender only need override necessary method
    }

    override fun onTransitionCancel(transition: Transition) {
        // Extender only need override necessary method
    }

    override fun onTransitionStart(transition: Transition) {
        // Extender only need override necessary method
    }

}