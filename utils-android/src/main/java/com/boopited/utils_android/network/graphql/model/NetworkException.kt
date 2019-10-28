package com.boopited.utils_android.network.graphql.model

import com.apollographql.apollo.exception.ApolloCanceledException
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import java.io.IOException

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

class NetworkException(throwable: Throwable): Exception(throwable.message) {

    val errorType = parseClientError(throwable)

    private fun parseClientError(throwable: Throwable): Kind {
        return when (throwable) {
            is ApolloHttpException -> {
                Kind.HTTP
            }
            is ApolloCanceledException -> {
                Kind.UNEXPECTED
            }
            is ApolloNetworkException -> {
                Kind.NETWORK
            }
            else -> {//ApolloException ApolloParseException
                Kind.HTTP
            }
        }
    }
}