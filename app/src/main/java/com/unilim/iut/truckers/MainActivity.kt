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
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity() {

    private val SMS_PERMISSION_CODE = 123

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérifiez et demandez la permission RECEIVE_SMS si nécessaire
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

    private fun scheduleAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SmsReceiverService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            60 * 1000, // toutes les minutes
            pendingIntent
        )
    }

    private fun createWhitelistFromJson(context: Context?) {
        val filePath = "whitelist.json"

        // Vérifier si le fichier existe déjà
        val file = File(context?.filesDir, filePath)
        if (file.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $filePath")
            return
        }

        // Création de la liste blanche
        val whitelist = listOf("1234567890", "9876543210", "5555555555")

        // Création de l'objet JSON
        val jsonObject = JSONObject()
        jsonObject.put("whitelist", JSONArray(whitelist))

        // Écriture de l'objet JSON dans le fichier
        try {
            // Ouverture du fichier en écriture dans le stockage interne de l'application
            val outputStream: FileOutputStream? = context?.openFileOutput(filePath, Context.MODE_PRIVATE)

            // Écriture de l'objet JSON dans le fichier
            outputStream?.write(jsonObject.toString(4).toByteArray())
            outputStream?.close()

            Log.d("SMSReceiver","Fichier JSON sauvegardé avec succès : $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
