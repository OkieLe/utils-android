package com.boopited.utils_android.rx

import io.reactivex.schedulers.Schedulers
import io.reactivex.Scheduler

class TestSchedulerGetter: SchedulerGetter {

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}