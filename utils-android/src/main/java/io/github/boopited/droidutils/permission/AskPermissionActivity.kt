package io.github.boopited.droidutils.permission

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class AskPermissionActivity: AppCompatActivity(), PermissionChecker {

    override val permissionsRequested: List<String>
        get() = RUNTIME_PERMISSIONS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasPermissions(this)) {
            requestPermissions(this, permissionsToAsk(this).toTypedArray(), REQUEST_PERMISSION)
        } else {
            onPermissionGranted()
        }
    }

    protected fun requestNow(): Boolean {
        return requestNotGranted(this, REQUEST_PERMISSION)
    }

    abstract fun onPermissionDenied()
    abstract fun onPermissionGranted()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION) {
            if (!isAllGranted(grantResults)) {
                onPermissionDenied()
            } else {
                onPermissionGranted()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val REQUEST_PERMISSION = 1001
        private val RUNTIME_PERMISSIONS = listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}