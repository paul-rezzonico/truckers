package com.unilim.iut.truckers.service

import android.content.Context
import android.content.IntentFilter
import android.provider.Telephony
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class ServiceDuReceveurDeSMS(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val smsReceiver = ReceveurDeSMS()

    override fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences("sms_service_prefs", Context.MODE_PRIVATE)
        val isReceiverRegistered = sharedPreferences.getBoolean("is_receiver_registered", false)

        if (!isReceiverRegistered) {
            val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            applicationContext.registerReceiver(smsReceiver, filter)

            sharedPreferences.edit().putBoolean("is_receiver_registered", true).apply()
            Log.d("TruckerService", "Service actif")
        }

        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        applicationContext.getSharedPreferences("sms_service_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("is_receiver_registered", false)
            .apply()
    }
}
