package com.boopited.utils_android.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

fun Context.hasPermissions(permissions: List<String>): Boolean {
    return !permissions.any {
        this.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.dialNumber(phoneNumber: String) {
    val number = if (phoneNumber.startsWith("tel:"))
        phoneNumber else "tel:$phoneNumber"
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(number))
    startActivity(intent)
}

fun Context.isPackageInstalled(pkgName: String):Boolean {
    return try {
        val packageInfo = packageManager.getPackageInfo(pkgName, 0)
        packageInfo != null
    } catch (e: Throwable) {
        false
    }
}