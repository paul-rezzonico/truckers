package com.unilim.iut.truckers.service

import android.content.Context
import android.content.IntentFilter
import android.provider.Telephony
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SmsReceiverService(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val smsReceiver = SmsReceiver()

    override fun doWork(): Result {
        val filtreIntention = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        applicationContext.registerReceiver(smsReceiver, filtreIntention)
        Log.d("TruckerService", "Service actif")

        return Result.success()
    }
}
