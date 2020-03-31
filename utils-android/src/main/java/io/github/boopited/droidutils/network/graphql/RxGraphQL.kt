package io.github.boopited.droidutils.network.graphql

import android.content.Context
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.fetcher.ResponseFetcher
import io.github.boopited.droidutils.network.graphql.impl.RxApolloGraphQL
import io.github.boopited.droidutils.network.graphql.model.ApolloConfig
import io.reactivex.Flowable
import okhttp3.Interceptor

interface RxGraphQL {

    fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> safeQuery(
        query: Query<D, T, V>,
        cachePolicy: HttpCachePolicy.Policy = HttpCachePolicy.NETWORK_ONLY,
        responseFetcher: ResponseFetcher = ApolloResponseFetchers.NETWORK_ONLY
    ): Flowable<T?>


    /**
     * Calls mutation on ApolloClient.
     * Catches errors and responds to expired sid.
     * This is the exposed entry point that repositories talk to when they need to make a mutation
     */
    fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> safeMutation(
        mutation: Mutation<D, T, V>
    ): Flowable<T?>

    /**
     * Called from logout.
     * Clears the normalized cache (on disk cache).
     * */
    fun clearData()

    companion object {
        fun get(context: Context, config: ApolloConfig,
                interceptors: List<Interceptor> = emptyList()): RxGraphQL
        {
            return RxApolloGraphQL(context, config, interceptors)
        }
    }
}