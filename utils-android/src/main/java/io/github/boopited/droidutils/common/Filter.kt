package io.github.boopited.droidutils.common

import java.util.*

/** Filters objects of type T.  */
abstract class Filter<T> {
    /**
     * Returns whether the specified object matches the filter.
     *
     * @param obj The object to filter.
     * @return `true` if the object is accepted.
     */
    abstract fun accept(obj: T): Boolean

    /**
     * Returns the logical AND of this and the specified filter.
     *
     * @param filter The filter to AND this filter with.
     * @return A filter where calling `accept()` returns the result of `
     * (this.accept() && filter.accept())`.
     */
    open fun and(filter: Filter<T>?): Filter<T> {
        return if (filter == null) {
            this
        } else FilterAnd(this, filter)
    }

    /**
     * Returns the logical OR of this and the specified filter.
     *
     * @param filter The filter to OR this filter with.
     * @return A filter where calling `accept()` returns the result of `
     * (this.accept() || filter.accept())`.
     */
    open fun or(filter: Filter<T>?): Filter<T> {
        return if (filter == null) {
            this
        } else FilterOr(this, filter)

    }

    private class FilterAnd<T>(lhs: Filter<T>, rhs: Filter<T>) : Filter<T>() {
        private val filters = LinkedList<Filter<T>>()

        init {
            filters.add(lhs)
            filters.add(rhs)
        }

        override fun accept(obj: T): Boolean {
            filters.forEach {
                if (!it.accept(obj)) {
                    return false
                }
            }

            return true
        }

        override fun and(filter: Filter<T>?): Filter<T> {
            filter?.let { filters.add(it) }
            return this
        }
    }

    private class FilterOr<T>(lhs: Filter<T>, rhs: Filter<T>) : Filter<T>() {
        private val filters = LinkedList<Filter<T>>()

        init {
            filters.add(lhs)
            filters.add(rhs)
        }

        override fun accept(obj: T): Boolean {
            filters.forEach {
                if (it.accept(obj)) {
                    return true
                }
            }

            return false
        }

        override fun or(filter: Filter<T>?): Filter<T> {
            filter?.let { filters.add(it) }
            return this
        }
    }
}