package com.boopited.utils_android.data

import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import java.io.File

/**
 * Replace all android.database.sqlite.* import statements with net.sqlcipher.database.*
 * (e.g., convert android.database.sqlite.SQLiteDatabase to net.sqlcipher.database.SQLiteDatabase)
 *
 * Before attempting to open a database, call SQLiteDatabase.loadLibs(), passing in a Context
 * (e.g., add this to onCreate() of your Application subclass,
 * using the Application itself as the Context)
 *
 * When opening a database (e.g., SQLiteDatabase.openOrCreateDatabase()),
 * pass in the passphrase as a char[] or byte[]
 */

fun encryptSqlite(context: Context, dbName: String, password: String) {
    val originalFile = context.getDatabasePath(dbName)

    if (originalFile.exists()) {
        val newFile = File.createTempFile("sqlcipherutils", "tmp", context.cacheDir)
        var db = SQLiteDatabase.openDatabase(originalFile.absolutePath,
                "", null,
                SQLiteDatabase.OPEN_READWRITE)

        db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
                newFile.absolutePath, password))
        db.rawExecSQL("SELECT sqlcipher_export('encrypted')")
        db.rawExecSQL("DETACH DATABASE encrypted;")

        val version = db.version
        db.close()

        db = SQLiteDatabase.openDatabase(newFile.absolutePath,
                password, null,
                SQLiteDatabase.OPEN_READWRITE)
        db.version = version
        db.close()

        originalFile.delete()
        newFile.renameTo(originalFile)
    }
}

fun decryptSqlite(context: Context, dbName: String, password: String, decFile: File): Boolean {
    var success = false
    decFile.delete()

    try {
        val originalFile = context.getDatabasePath(dbName)
        val database = SQLiteDatabase.openOrCreateDatabase(originalFile, password, null)

        if (database.isOpen) {
            database.rawExecSQL(String.format("ATTACH DATABASE '%s' as plaintext KEY '';", decFile.absolutePath))
            database.rawExecSQL("SELECT sqlcipher_export('plaintext');")
            database.rawExecSQL("DETACH DATABASE plaintext;")
            val sqlDB = android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(decFile, null)
            success = sqlDB != null
            sqlDB?.close()
            database.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return success
}