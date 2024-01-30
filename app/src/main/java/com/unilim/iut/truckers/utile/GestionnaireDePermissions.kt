package com.unilim.iut.truckers.utile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GestionnaireDePermissions(private val activite: Activity) {

    private val SMS_RECEIVE_PERMISSION_CODE = 123
    private val SMS_READ_PERMISSION_CODE = 124

    fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", activite.packageName, null)
                intent.data = uri
                activite.startActivity(intent)
            } else {
                requestSMSPermissions()
            }
        } else {
            requestSMSPermissions()
        }
    }

    fun requestSMSPermissions() {
        if (ContextCompat.checkSelfPermission(activite, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activite, arrayOf(android.Manifest.permission.RECEIVE_SMS), SMS_RECEIVE_PERMISSION_CODE)
        }
        if (ContextCompat.checkSelfPermission(activite, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activite, arrayOf(android.Manifest.permission.READ_SMS), SMS_READ_PERMISSION_CODE)
        }
        if (ContextCompat.checkSelfPermission(activite, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activite, arrayOf(android.Manifest.permission.READ_PHONE_STATE), SMS_READ_PERMISSION_CODE)
        }
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray, callback: (Boolean) -> Unit) {
        if (requestCode == SMS_RECEIVE_PERMISSION_CODE || requestCode == SMS_READ_PERMISSION_CODE) {
            val isPermissionGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            callback(isPermissionGranted)
        }
    }

    fun areAllPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activite, android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activite, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activite, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
    }
}