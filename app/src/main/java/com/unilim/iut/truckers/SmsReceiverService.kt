package com.unilim.iut.truckers

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class SmsReceiverService : Service() {

    private val smsReceiver = SmsReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, intentFilter)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }
}
