package com.boopited.utils_android.network.restful.interceptor

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetryWithDelayFunc(private val retryTimes: Int, private val delayMillis: Long)
    : Function<Observable<out Throwable>, Observable<*>> {

    override fun apply(errors: Observable<out Throwable>): Observable<*> {
        return errors.zipWith(Observable.range(1, retryTimes + 1),
                BiFunction<Throwable, Int, Pair<Throwable, Int>> { error, retryCount -> Pair(error, retryCount) })
                .flatMap {
                    if (it.first is IOException) {
                        Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                    } else {
                        Observable.error(it.first)
                    }
                }
    }

}