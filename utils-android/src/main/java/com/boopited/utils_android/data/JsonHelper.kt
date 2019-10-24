package com.boopited.utils_android.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

object JsonUtils {

    val sGson: Gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .create()
    }

    fun toJson(obj: Any?): String {
        return sGson.toJson(obj)
    }

    inline fun <reified T> fromJson(json: String?): T? {
        return sGson.fromJson<T>(json, object : TypeToken<T>() {}.type)
    }

    fun createConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create(sGson)
    }
}

interface IJson : Serializable {

    fun toJson(): String {
        return try {
            JsonUtils.sGson.toJson(this, this::class.java)
        } catch (e: Throwable) {
            ""
        }
    }
}

inline fun <reified T : IJson> String?.fromJson(): T? {
    if (this.isNullOrEmpty()) return null
    return try {
        val type = object : TypeToken<T>() {}.type
        JsonUtils.sGson.fromJson(this, type)
    } catch (e: Throwable) {
        null
    }
}