package com.boopited.utils_android.app

import android.app.ActivityManager
import android.content.Context
import android.os.Process

object ProcessHelper {
    @Volatile
    private var currentProcessName: String = ""

    fun isMainProcess(ctx: Context): Boolean {
        return ctx.packageName == getCurrentProcessName(ctx)
    }

    private fun getCurrentProcessName(ctx: Context): String {
        if (currentProcessName.isNotEmpty())
            return currentProcessName

        val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processesInfo = am.runningAppProcesses
        val myPid = Process.myPid()
        currentProcessName = processesInfo?.find { it.pid == myPid }?.processName.orEmpty()
        return currentProcessName
    }
}
