package com.boopited.utils_android.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import java.io.*

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

private val BUFFER_SIZE = 1024

//read file from android data directory
fun Context.readFile(pathName: String): Single<String> {
    return Single.create { e ->
        try {
            val fileInputStream = openFileInput(pathName)
            val b = ByteArray(BUFFER_SIZE)
            var n: Int
            val byteArrayOutputStream = ByteArrayOutputStream()
            while (true) {
                fileInputStream.read(b).takeIf { it > -1 }?.let {
                    byteArrayOutputStream.write(b, 0, it)
                } ?: break
            }
            val content = byteArrayOutputStream.toByteArray()
            e.onSuccess(String(content))
        } catch (e1: IOException) {
            e.onError(e1)
        }
    }
}

//write file to android data directory
fun Context.writeFile(pathName: String, text: String): Single<Any> {
    return Single.create { e ->
        try {
            val fileOutputStream = openFileOutput(pathName, MODE_PRIVATE)
            fileOutputStream.write(text.toByteArray())
            fileOutputStream.close()

            e.onSuccess(Any())
        } catch (e1: IOException) {
            e.onError(e1)
        }
    }
}

fun Context.readAssetsFile(fileName: String): Single<String> {
    return Single.create(SingleOnSubscribe<String> { e ->
        try {
            val br = BufferedReader(InputStreamReader(assets.open(fileName)))
            val text = StringBuilder()

            while (true) {
                br.readLine()?.let {
                    text.append("$it\n")
                } ?: break
            }

            e.onSuccess(text.toString().substring(0, text.length - 1))
        } catch (e1: IOException) {
            e.onError(e1)
        }
    })
}

fun Context.readSDCardFile(fileName: String): Single<String> {
    return Single.create { e ->
        try {
            val sdcard = getExternalFilesDir(null)
            val file = File(sdcard, fileName)
            val text = StringBuilder()
            val br = BufferedReader(FileReader(file))

            while (true) {
                br.readLine()?.let {
                    text.append("$it\n")
                } ?: break
            }
            e.onSuccess(text.toString().substring(0, text.length - 1))

        } catch (e1: IOException) {
            e.onError(e1)
        }
    }
}

fun Context.writeSDCardFile(filename: String, text: String): Single<Any> {
    return Single.create { e ->
        try {
            val sdcard = getExternalFilesDir(null)
            val os = FileOutputStream(File(sdcard, filename))
            os.write(text.toByteArray())
            os.close()
            e.onSuccess(Any())
        } catch (e1: IOException) {
            e.onError(e1)
        }
    }
}

fun File.walkThrough(action: (File) -> Unit, recursive: Boolean = false) {
    val files = listFiles() ?: return

    files.forEach { file ->
        val absFile = file.absoluteFile
        if (file.isDirectory && recursive) {
            file.walkThrough(action, true)
        }
        action.invoke(absFile)
    }
}