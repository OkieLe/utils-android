package com.boopited.utils_android.network.adapter

import androidx.lifecycle.LiveData
import com.boopited.utils_android.network.model.ApiResponse
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }

        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)

        require(rawObservableType == ApiResponse::class.java) { "type must be a resource" }
        require(observableType is ParameterizedType) { "resource must be parameterized" }

        val bodyType = getParameterUpperBound(0, observableType)
        return LiveDataCallAdapterWrapper<Any>(bodyType)
    }

    private class LiveDataCallAdapterWrapper<R>(private val responseType: Type)
        : CallAdapter<R, LiveData<ApiResponse<R>>>
    {

        override fun responseType() = responseType

        override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
            return object : LiveData<ApiResponse<R>>() {
                private var started = AtomicBoolean(false)
                override fun onActive() {
                    super.onActive()
                    if (started.compareAndSet(false, true)) {
                        call.enqueue(object : Callback<R> {
                            override fun onResponse(call: Call<R>, response: Response<R>) {
                                postValue(ApiResponse.create(response))
                            }
                            override fun onFailure(call: Call<R>, throwable: Throwable) {
                                postValue(ApiResponse.create(throwable))
                            }
                        })
                    }
                }
            }
        }
    }
}