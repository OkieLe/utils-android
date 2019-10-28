package com.boopited.utils_android.network.adapter

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxThreadCallAdapter private constructor(
    private val subscribeScheduler: Scheduler,
    private val observerScheduler: Scheduler) : CallAdapter.Factory()
{

    private val original: RxJava2CallAdapterFactory =
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    private class RxCallAdapterWrapper<R>
    internal constructor(private val wrapped: CallAdapter<R, *>,
                         private val subscribeScheduler: Scheduler,
                         private val observerScheduler: Scheduler)
        : CallAdapter<R, Observable<*>>
    {

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun adapt(call: Call<R>): Observable<*> {
            val observable = wrapped.adapt(call) as Observable<*>
            return observable.subscribeOn(subscribeScheduler).observeOn(observerScheduler)
        }
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return original.get(returnType, annotations, retrofit)?.let {
            RxCallAdapterWrapper(it, subscribeScheduler, observerScheduler)
        }
    }

    companion object {

        fun create(subscribeScheduler: Scheduler = Schedulers.io(),
                   observerScheduler: Scheduler = AndroidSchedulers.mainThread())
                : CallAdapter.Factory {
            return RxThreadCallAdapter(subscribeScheduler, observerScheduler)
        }
    }
}