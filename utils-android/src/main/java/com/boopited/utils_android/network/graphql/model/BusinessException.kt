package com.boopited.utils_android.network.graphql.model

data class BusinessException(override val message: String): Exception(message)