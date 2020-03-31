package io.github.boopited.droidutils.common

import org.greenrobot.eventbus.EventBus

abstract class BaseEvent(val eventId: String = this::class.java.simpleName) {

    companion object {
        fun register(any: Any) {
            if (isRegistered(any)) return
            EventBus.getDefault().register(any)
        }

        fun unregister(any: Any) {
            if (!isRegistered(any)) return
            EventBus.getDefault().unregister(any)
        }

        fun isRegistered(any: Any): Boolean {
            return EventBus.getDefault().isRegistered(any)
        }
    }
}

fun BaseEvent.post() {
    EventBus.getDefault().post(this)
}