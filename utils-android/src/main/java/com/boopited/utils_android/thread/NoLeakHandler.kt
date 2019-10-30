package com.boopited.utils_android.thread

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

abstract class NoLeakHandler<T>(parent: T): Handler() {

    private val parentRef: WeakReference<T> = WeakReference(parent)

    protected val parent: T?
        get() = parentRef.get()

    override fun handleMessage(msg: Message) {
        val parent = parent ?: return

        handleMessage(msg, parent)
    }

    protected abstract fun handleMessage(msg: Message, parent: T)
}