package com.unilim.iut.truckers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unilim.iut.truckers.controller.MessageController
import com.unilim.iut.truckers.controller.WhiteListController
import com.unilim.iut.truckers.service.SmsReceiverService
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    private val controlleurListeBlanche = WhiteListController()
    private val controllerMessage = MessageController()
    private val SMS_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), SMS_PERMISSION_CODE)
        }

        controlleurListeBlanche.creationListeBlanche(this)
        controllerMessage.creationJsonMauvaisMessage(this)
        controllerMessage.creationJsonBonMessage(this)

        val workManager = WorkManager.getInstance(this)
        val smsWorkerRequest = PeriodicWorkRequest.Builder(SmsReceiverService::class.java, 15, TimeUnit.MINUTES)
            .build()

        workManager.enqueue(smsWorkerRequest)

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
