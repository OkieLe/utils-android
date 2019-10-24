package com.boopited.utils_android.common

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.schedulers.Schedulers

fun <T> Flowable<T>.transIOToUI(): Flowable<T> = this.subscribeOnIO().observeOnMain()
fun <T> Flowable<T>.transIOToIO(): Flowable<T> = this.subscribeOnIO().observeOnIO()
fun <T> Flowable<T>.transUIToUI(): Flowable<T> = this.subscribeOnMain().observeOnMain()
fun <T> Flowable<T>.observeOnIO(): Flowable<T> = this.observeOn(Schedulers.io())
fun <T> Flowable<T>.observeOnMain(): Flowable<T> = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Flowable<T>.subscribeOnMain(): Flowable<T> = this.subscribeOn(AndroidSchedulers.mainThread())
fun <T> Flowable<T>.subscribeOnIO(): Flowable<T> = this.subscribeOn(Schedulers.io())

fun Disposable.addToComposite(container: DisposableContainer): Disposable {
    container.add(this)
    return this
}