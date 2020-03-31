package io.github.boopited.droidutils.network.restful.adapter

import io.github.boopited.droidutils.network.restful.model.RetrofitException
import io.reactivex.*
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class RxErrorHandlingCallAdapter private constructor() : CallAdapter.Factory() {

    private val original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    companion object {
        fun create(): CallAdapter.Factory =
            RxErrorHandlingCallAdapter()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        val wrapped = original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        return RxCallAdapterWrapper(
            retrofit,
            wrapped
        )
    }

    private class RxCallAdapterWrapper<R>(private val retrofit: Retrofit, private val wrapped: CallAdapter<R, *>) :
        CallAdapter<R, Any> {

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun adapt(call: Call<R>): Any {
            when (val adaptedCall = wrapped.adapt(call)) {
                is Completable -> {
                    return adaptedCall.onErrorResumeNext {
                        Completable.error(asRetrofitException(it))
                    }
                }

                is Single<*> -> {
                    return adaptedCall.onErrorResumeNext {
                        Single.error(asRetrofitException(it))
                    }
                }

                is Observable<*> -> {
                    return adaptedCall.onErrorResumeNext(Function {
                        Observable.error(asRetrofitException(it))
                    })
                }

                is Flowable<*> -> {
                    return adaptedCall.onErrorResumeNext(Function {
                        Flowable.error(asRetrofitException(it))
                    })
                }

                is Maybe<*> -> {
                    return adaptedCall.onErrorResumeNext(Function {
                        Maybe.error(asRetrofitException(it))
                    })
                }

                else ->
                    throw RuntimeException("Observable Type not supported")
            }
        }

        private fun asRetrofitException(throwable: Throwable): RetrofitException {
            return when (throwable) {
                is HttpException -> {
                    // We had non-200 http error
                    val response = throwable.response()
                    RetrofitException.httpError(
                        response?.raw()?.request?.url?.toString().orEmpty(),
                        response,
                        retrofit
                    )
                }
                is SocketTimeoutException -> {
                    RetrofitException.unexpectedError(throwable)
                }
                is IOException -> {
                    // A network error happened
                    RetrofitException.networkError(throwable)
                }
                else -> {
                    // We don't know what happened. We need to simply convert to an unknown error
                    RetrofitException.unexpectedError(throwable)
                }
            }
        }
    }
}