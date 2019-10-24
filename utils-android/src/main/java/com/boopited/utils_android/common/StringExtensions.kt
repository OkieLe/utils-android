package com.boopited.utils_android.common

import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

fun String?.trimSpace():String {
    var dest = ""
    if (!this.isNullOrBlank()) {
        val p = Pattern.compile ("\\s*|\t|\r|\n");
        val m = p.matcher (this)
        dest = m.replaceAll("")
    }
    return dest
}

fun String.toDBC(): String {
    val c = this.toCharArray()
    for (i in c.indices) {
        if (c[i].toInt() == 12288) {
            c[i] = 32.toChar()
            continue
        }
        if (c[i].toInt() in 65281..65374)
            c[i] = (c[i].toInt() - 65248).toChar()
    }
    return String(c)
}

fun String.sha1(): String {
    return this.hashWithAlgorithm("SHA-1")
}

fun String.toDate(): Date {
    return if (this.isEmpty()) {
        Date()
    } else {
        Date(this.toLong())
    }
}

private fun String.hashWithAlgorithm(algorithm: String): String {
    val digest = MessageDigest.getInstance(algorithm)
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("") { str, it -> str + "%02x".format(it) }
}