package io.github.boopited.droidutils.network.graphql.adapter

import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import java.math.BigDecimal

class BigDecimalAdapter : CustomTypeAdapter<BigDecimal> {
    override fun decode(value: CustomTypeValue<*>): BigDecimal {
        return BigDecimal(value.value.toString())
    }

    override fun encode(value: BigDecimal): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLString(value.toPlainString())
    }
}