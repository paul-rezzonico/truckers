package com.unilim.iut.truckers

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import com.unilim.iut.truckers.model.PhoneNumber
import com.unilim.iut.truckers.service.SmsReceiverService
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity() {

    private val SMS_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), SMS_PERMISSION_CODE)
        }

        createWhitelistFromJson(this)
        startService(Intent(this, SmsReceiverService::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun createWhitelistFromJson(context: Context?) {
        val filePath = "whitelist.json"

        val file = File(context?.filesDir, filePath)
        if (file.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $filePath")
            return
        }

        val whitelist = listOf(PhoneNumber("0123456789").toString(), PhoneNumber("0987654321").toString(), PhoneNumber("0555555555").toString())

        val jsonObject = JSONObject()
        jsonObject.put("whitelist", JSONArray(whitelist))

        try {
            val outputStream: FileOutputStream? = context?.openFileOutput(filePath, Context.MODE_PRIVATE)

            outputStream?.write(jsonObject.toString(4).toByteArray())
            outputStream?.close()

            Log.d("SMSReceiver","Fichier JSON sauvegardé avec succès : $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
