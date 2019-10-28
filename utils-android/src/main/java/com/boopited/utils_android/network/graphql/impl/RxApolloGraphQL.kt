package com.boopited.utils_android.network.graphql.impl

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.*
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.fetcher.ResponseFetcher
import com.apollographql.apollo.rx2.Rx2Apollo
import com.boopited.utils_android.network.graphql.RxGraphQL
import com.boopited.utils_android.network.graphql.model.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class RxApolloGraphQL(
    private val context: Context,
    private val config: ApolloConfig,
    private val interceptors: List<Interceptor>
) : RxGraphQL {

    @Volatile
    private var client: ApolloClient? = null

    private fun getClient(): ApolloClient {
        return client ?: synchronized(this) {
            client ?: buildApolloClient().also {
                client = it
            }
        }
    }

    override fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables>
            safeQuery(query: Query<D, T, V>,
                      cachePolicy: HttpCachePolicy.Policy,
                      responseFetcher: ResponseFetcher): Flowable<T?>
    {
        val queryCall = getClient().query(query)
            .httpCachePolicy(cachePolicy).responseFetcher(responseFetcher)
        return Rx2Apollo.from<T>(queryCall).toFlowable(BackpressureStrategy.BUFFER)
            .compose(handleResult())
    }

    override fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables>
            safeMutation(mutation: Mutation<D, T, V>): Flowable<T?>
    {
        val mutationCall = getClient().mutate(mutation)
        return Rx2Apollo.from<T>(mutationCall).toFlowable(BackpressureStrategy.BUFFER)
            .compose(handleResult())
    }

    private fun <T> handleResult(): FlowableTransformer<Response<T>, T>
    {
        return FlowableTransformer { responseFlowable ->
            responseFlowable
                .flatMap<T> {
                    if (it.hasErrors()) {
                        val error = it.errors().first()
                        Flowable.error<T>(BusinessException(error.message() ?: ""))
                    }
                    if (it.data() == null) {
                        Flowable.error<T>(NoDataException())
                    }
                    Flowable.just(it.data())
                }
                .onErrorResumeNext { throwable: Throwable ->
                    if (throwable is BusinessException || throwable is NoDataException) {
                        return@onErrorResumeNext Flowable.error(throwable)
                    }
                    return@onErrorResumeNext Flowable.error<T>(NetworkException(throwable))
                }
        }
    }

    override fun clearData() {
        getClient().clearNormalizedCache()
    }

    private fun buildApolloClient(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder().apply {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            addInterceptor(httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = config.httpLogLevel
            })
            interceptors.forEach { addInterceptor(it) }
        }.build()

        val apolloSqlHelper = ApolloSqlHelper.create(context, config.dbName)

        val cacheFactory = SqlNormalizedCacheFactory(apolloSqlHelper)
        val resolver = object : CacheKeyResolver() {

            override fun fromFieldRecordSet(
                field: ResponseField,
                recordSet: MutableMap<String, Any>
            ): CacheKey {
                if (recordSet.containsKey("id")) {
                    val typeNameAndIDKey = recordSet["__typename"].toString() + "." + recordSet["id"]
                    return CacheKey.from(typeNameAndIDKey)
                }
                return CacheKey.NO_KEY
            }

            override fun fromFieldArguments(
                field: ResponseField,
                variables: Operation.Variables
            ): CacheKey {
                return CacheKey.NO_KEY
            }
        }

        return ApolloClient.builder()
            .serverUrl(config.baseServerUrl)
            .okHttpClient(okHttpClient)
            .normalizedCache(cacheFactory, resolver)
            .also {
                config.customTypes.forEach { (type, adapter) ->
                    it.addCustomTypeAdapter(type, adapter)
                }
            }
            .build()
    }
}