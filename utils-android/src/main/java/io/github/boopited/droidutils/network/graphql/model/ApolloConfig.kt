package io.github.boopited.droidutils.network.graphql.model

import com.apollographql.apollo.api.ScalarType
import com.apollographql.apollo.response.CustomTypeAdapter
import okhttp3.logging.HttpLoggingInterceptor

data class ApolloConfig(
    val baseServerUrl: String,
    val dbName: String,
    val customTypes: Map<ScalarType, CustomTypeAdapter<*>> = emptyMap(),
    val httpLogLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
)