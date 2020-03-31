package io.github.boopited.droidutils.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.EXTRA_APP_PACKAGE
import android.provider.Settings.EXTRA_CHANNEL_ID
import androidx.core.app.NotificationManagerCompat

fun goToNotificationSettings(context: Activity) {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(EXTRA_APP_PACKAGE, context.packageName)
        intent.putExtra(EXTRA_CHANNEL_ID, context.applicationInfo.uid)
        intent.putExtra("app_package", context.packageName)
        intent.putExtra("app_uid", context.applicationInfo.uid)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        }catch (e:Exception) {
            goToAppSettings(context)
        }
    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
        goToAppSettings(context)
    } else {
        goToSystemSettings(context)
    }
}

fun goToPermissionSettings(context: Activity) {
    goToAppSettings(context)
}

private fun goToAppSettings(context: Activity) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.data = Uri.parse("package:" + context.packageName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(intent)
    } catch (e:Exception) {
        e.printStackTrace()
        goToSystemSettings(context)
    }

}

private fun goToSystemSettings(context: Activity) {
    val intent = Intent()
    intent.action = Settings.ACTION_SETTINGS
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun isNotificationEnabled(context: Context): Boolean {
    val manager = NotificationManagerCompat.from(context)
    return manager.areNotificationsEnabled()
}