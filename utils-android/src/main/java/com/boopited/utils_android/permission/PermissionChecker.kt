package com.boopited.utils_android.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager

interface PermissionChecker {

    val permissionsRequested: List<String>

    fun hasPermissions(context: Context): Boolean {
        return permissionsRequested.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun permissionsToAsk(context: Context): List<String> {
        return permissionsRequested.filter {
            context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(context: Activity, permissions: Array<String>, requestCode: Int) {
        context.requestPermissions(permissions, requestCode)
    }

    fun isAllGranted(results: IntArray): Boolean {
        return results.all { it == PackageManager.PERMISSION_GRANTED }
    }

    fun requestNotGranted(context: Activity, requestCode: Int): Boolean {
        val permissions = permissionsToAsk(context)
        return if (permissions.isNotEmpty()) {
            context.requestPermissions(permissions.toTypedArray(), requestCode)
            true
        } else {
            false
        }
    }
}