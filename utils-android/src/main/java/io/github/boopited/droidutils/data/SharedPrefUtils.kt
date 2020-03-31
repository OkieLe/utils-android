package io.github.boopited.droidutils.data

import android.content.SharedPreferences

/**
 * May be replaced with core-ktx
 */
inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> {
            val default = defaultValue as? Int ?: -1
            val int = getInt(key, default)
            return if (int == default) {
                defaultValue
            } else {
                int as T?
            }
        }
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
        Float::class -> {
            val default = defaultValue as? Float ?: -1f
            val float = getFloat(key, default)
            return if (float == default) {
                defaultValue
            } else {
                float as T?
            }
        }
        Long::class -> {
            val default = defaultValue as? Long ?: -1L
            val long = getLong(key, default)
            return if (long == default) {
                defaultValue
            } else {
                long as T?
            }
        }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}