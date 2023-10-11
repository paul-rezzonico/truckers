package com.unilim.iut.truckers.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.provider.Telephony

class SmsReceiverService : Service() {

    private val smsReceiver = SmsReceiver()

    override fun onStartCommand(intention: Intent?, drapeaux: Int, idDebut: Int): Int {
        val filtreIntention = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsReceiver, filtreIntention)

        return START_STICKY
    }

    override fun onBind(intention: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }
}
