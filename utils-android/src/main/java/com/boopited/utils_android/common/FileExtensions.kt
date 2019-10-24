package com.boopited.utils_android.common

import java.io.File

fun File.childFolder(childName: String): File? {
    val childFolder = File(this, childName)
    if (childFolder.exists()) {
        if (!childFolder.isDirectory) {
            return null
        }
        return childFolder
    }
    if (!childFolder.mkdirs()) {
        return null
    }
    return childFolder
}

fun File.child(childName: String): File = File(this, childName)