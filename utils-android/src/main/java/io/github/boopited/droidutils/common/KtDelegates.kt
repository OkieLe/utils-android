package io.github.boopited.droidutils.common

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> Delegates.notNullSingleValue() =
    NotNullSingleValueVar<T>()

class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("${property.name} not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) {
            value
        } else {
            throw IllegalStateException("${property.name} already initialized")
        }
    }
}