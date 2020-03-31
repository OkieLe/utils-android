package io.github.boopited.droidutils.rx

import io.reactivex.Scheduler

interface SchedulerGetter {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}