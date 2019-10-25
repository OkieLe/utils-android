package com.boopited.utils_android.app

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.content.Intent

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

    fun restart(context: Context, applicationId: String) {
        killBackgroundProcesses(context, applicationId)
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                super.run()
                val packageManager = context.packageManager
                val intent = packageManager.getLaunchIntentForPackage(context.packageName)
                val componentName = intent!!.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                context.applicationContext.startActivity(mainIntent)

            }
        })
        Runtime.getRuntime().exit(0)
    }

    fun suicide(context: Context, applicationId: String) {
        killBackgroundProcesses(context, applicationId)
        Runtime.getRuntime().exit(0)
    }

    fun startApplication(context: Context, packageName: String) {
        context.startActivity(context.packageManager.getLaunchIntentForPackage(packageName))
    }

    private fun killBackgroundProcesses(context: Context, applicationId: String) {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = manager.runningAppProcesses
        if (runningProcesses != null) {
            for (processInfo in runningProcesses) {
                if (!applicationId.equals(processInfo.processName, ignoreCase = true)) {
                    Process.killProcess(processInfo.pid)
                }
            }
        }
    }
}
