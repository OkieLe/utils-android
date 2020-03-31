package io.github.boopited.droidutils.network.graphql.model

data class BusinessException(override val message: String): Exception(message)