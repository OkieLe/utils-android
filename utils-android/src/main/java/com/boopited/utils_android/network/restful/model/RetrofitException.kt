package com.boopited.utils_android.network.restful.model

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class RetrofitException(
    message: String?,
    exception: Throwable?,
    val url: String?,
    val response: Response<*>?,
    val kind: Kind,
    val retrofit: Retrofit? = null
) : RuntimeException(message, exception) {

    companion object {

        fun httpError(url: String, response: Response<*>?, retrofit: Retrofit): RetrofitException {
            val message = response?.code().toString() + " " + response?.message()
            return RetrofitException(
                message,
                null,
                url,
                response,
                Kind.HTTP,
                retrofit
            )
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(
                exception.message,
                exception,
                null,
                null,
                Kind.NETWORK
            )
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(
                exception.message,
                exception,
                null,
                null,
                Kind.UNEXPECTED
            )
        }
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        response?.errorBody()?.let { errorBody ->
            retrofit?.let { retrofit ->
                val converter: Converter<ResponseBody, T> =
                    retrofit.responseBodyConverter(type, arrayOfNulls<Annotation>(0))
                return converter.convert(errorBody)
            }
        }
        return null
    }

    enum class Kind {
        /** An [IOException] occurred while communicating to the server.  */
        NETWORK,
        /** A non-200 HTTP status code was received from the server.  */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }
}