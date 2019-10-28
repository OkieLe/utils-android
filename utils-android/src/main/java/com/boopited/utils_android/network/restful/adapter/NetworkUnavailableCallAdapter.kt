package com.boopited.utils_android.network.restful.adapter

import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.net.UnknownHostException

class NetworkUnavailableCallAdapter : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit):
            CallAdapter<Any, *>? {

        val nextAdapterDelegate = retrofit.nextCallAdapter(this, returnType, annotations)

        @Suppress("UNCHECKED_CAST")
        return when (getRawType(returnType)) {
            Single::class.java -> SingleAdapter(
                nextAdapterDelegate
                        as CallAdapter<Any, Single<Response<Any>>>
            )

            Flowable::class.java -> FlowableAdapter(
                nextAdapterDelegate
                        as CallAdapter<Any, Flowable<Response<Any>>>
            )
            else -> null
        }
    }
}

private class SingleAdapter(private val nextAdapter: CallAdapter<Any, Single<Response<Any>>>) :
    CallAdapter<Any, Single<out Response<Any>>> {

    override fun adapt(call: Call<Any>): Single<Response<Any>> =
        nextAdapter.adapt(call).onErrorReturn(::handleUhe)

    override fun responseType(): Type = nextAdapter.responseType()
}

private class FlowableAdapter(private val nextAdapter: CallAdapter<Any, Flowable<Response<Any>>>) :
    CallAdapter<Any, Flowable<out Response<Any>>> {

    override fun adapt(call: Call<Any>) =
        nextAdapter.adapt(call).onErrorReturn(::handleUhe)

    override fun responseType(): Type = nextAdapter.responseType()
}

private fun handleUhe(throwable: Throwable): Response<Any> = when (throwable) {
    is UnknownHostException -> {
        val message = throwable.message ?: "No internet"
        Response.error<Any>(999, message.toResponseBody(null))
    }

    else -> throw throwable
}