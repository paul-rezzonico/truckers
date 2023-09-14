package com.unilim.iut.truckers

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.unilim.iut.truckers.SmsReceiverService

class MainActivity : ComponentActivity() {

    private val SMS_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérifiez et demandez la permission RECEIVE_SMS si nécessaire
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
        }

        // Démarrez le service SmsReceiverService pour écouter les SMS en arrière-plan
        val intent = Intent(this, SmsReceiverService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Vous pouvez arrêter le service ici si nécessaire
        val intent = Intent(this, SmsReceiverService::class.java)
        stopService(intent)
    }
}
