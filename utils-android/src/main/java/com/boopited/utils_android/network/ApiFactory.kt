package com.boopited.utils_android.network

import android.net.TrafficStats
import com.boopited.utils_android.data.JsonUtils
import com.boopited.utils_android.debug.isDevMode
import com.boopited.utils_android.network.interceptor.DelegatingSocketFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.net.Socket
import java.util.concurrent.TimeUnit

object ApiFactory {

    private const val CONNECTION_TIMEOUT = 15L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    fun <T> create(
            baseUrl: String, api: Class<T>,
            readTimeout: Long = READ_TIMEOUT,
            interceptors: List<Interceptor>? = null
    ): T {
        val socketFactory = object : DelegatingSocketFactory(getDefault()) {
            override fun configureSocket(socket: Socket) {
                TrafficStats.setThreadStatsTag(socket.hashCode())
            }
        }

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLogging())
                .socketFactory(socketFactory)

        interceptors?.forEach { clientBuilder.addInterceptor(it) }

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(JsonUtils.createConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(api)
    }

    private fun httpLogging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (isDevMode())
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.BASIC
        }
    }
}