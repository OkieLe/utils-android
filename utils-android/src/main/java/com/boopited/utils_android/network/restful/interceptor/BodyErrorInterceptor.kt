package com.boopited.utils_android.network.restful.interceptor

import okhttp3.Response
import com.boopited.utils_android.network.restful.model.ApiException
import org.json.JSONObject

class BodyErrorInterceptor : ResponseBodyInterceptor() {

    override fun intercept(response: Response, url: String, body: String): Response {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (jsonObject != null) {
            if (jsonObject.optInt("code", -1) != 200 && jsonObject.has("msg")) {
                throw ApiException(
                    jsonObject.getString(
                        "msg"
                    )
                )
            }
        }
        return response
    }
}
