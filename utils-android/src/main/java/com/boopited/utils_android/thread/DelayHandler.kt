package com.boopited.utils_android.thread

import android.os.Handler
import android.os.Message

abstract class DelayHandler<T> : Handler() {

    fun delay(delayMs: Long, handlerArg: T) {
        val message = obtainMessage(MESSAGE_ID, handlerArg)
        sendMessageDelayed(message, delayMs)
    }

    fun removeMessages() {
        removeMessages(MESSAGE_ID)
    }

    override fun handleMessage(message: Message) {
        if (message.what == MESSAGE_ID) {
            val messageObj = message.obj as T
            handle(messageObj)
        }
    }

    /** Method that will be called after delay.  */
    abstract fun handle(arg: T)

    companion object {
        private const val MESSAGE_ID = 1
    }
}