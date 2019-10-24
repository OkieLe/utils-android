package com.boopited.utils_android.resource

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File
import java.lang.Exception

fun isContentUri(uri: Uri): Boolean {
    return ContentResolver.SCHEME_CONTENT == uri.scheme
}

fun isFileUri(uri: Uri): Boolean {
    return ContentResolver.SCHEME_FILE == uri.scheme
}

fun isMediaStoreUri(uri: Uri): Boolean {
    return isContentUri(uri) && MediaStore.AUTHORITY == uri.authority
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

fun isGooglePlayPhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.contentprovider" == uri.authority
}

fun isFileProviderUri(context: Context, uri: Uri): Boolean {
    val packageName = context.packageName
    val authority = StringBuilder(packageName).append(".provider").toString()
    return authority == uri.authority
}

fun getFileProviderPath(context: Context, uri: Uri): String? {
    val appDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(appDir, uri.lastPathSegment!!)
    return if (file.exists()) file.toString() else null
}

private fun getMediaPath(context: Context, uri: Uri?,
                         selection: String? = null, selectionArgs: Array<String> = emptyArray()): String? {
    if (uri == null) return null
    val projection = arrayOf(MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.MIME_TYPE)
    var cursor: Cursor? = null
    try {
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0)
        }
    } finally {
        cursor?.close()
    }
    return null
}

private fun getDataColumn(context: Context, uri: Uri?,
                          selection: String? = null, selectionArgs: Array<String> = emptyArray()): String? {
    if (uri == null) return null
    val projection = arrayOf("_data")
    var cursor: Cursor? = null
    try {
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val path = cursor.getString(0)
            return path
        }
    } catch (e: Exception) {
        //Ignore
    } finally {
        cursor?.close()
    }
    return null
}

private fun parseGooglePlayPhotoPath(context: Context, uri: Uri): String {
    return uri.pathSegments.find { it.startsWith("content") }
            ?.let { getMediaPath(context, uri) }.orEmpty()
}

fun getPathFromURI(context: Context, uri: Uri): String? {
    val aboveKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    if (isFileUri(uri)) {
        return uri.path
    } else if (isMediaStoreUri(uri)) {
        return getMediaPath(context, uri, null, emptyArray())
    } else if (aboveKitkat && DocumentsContract.isDocumentUri(context, uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        if (isExternalStorageDocument(uri)) {
            // ExternalStorageProvider
            val split = docId.split(":")
            if (split.size < 2) return null

            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().absolutePath + File.pathSeparator + split[1]
            }
        } else if (isDownloadsDocument(uri)) {
            // DownloadsProvider
            if (docId.startsWith("raw:")) {
                return docId.substring(4)
            }
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    docId.toLong())
            return getDataColumn(context, contentUri)
        } else if (isMediaDocument(uri)) {
            // MediaProvider
            val split = docId.split(":")
            if (split.size < 2) return null

            val contentUri = when (split[0]) {
                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                else -> null
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            return getMediaPath(context, contentUri, selection, selectionArgs)
        }
    } else if (isContentUri(uri)) {
        return if (isGooglePhotosUri(uri))
            uri.lastPathSegment
        else if (isGooglePlayPhotosUri(uri))
            parseGooglePlayPhotoPath(context, uri)
        else if (isFileProviderUri(context, uri))
            getFileProviderPath(context, uri)
        else
            getDataColumn(context, uri)
    }
    return null
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}