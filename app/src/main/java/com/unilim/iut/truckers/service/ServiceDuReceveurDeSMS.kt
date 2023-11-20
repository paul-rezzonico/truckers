package com.unilim.iut.truckers.service

import android.content.Context
import android.content.IntentFilter
import android.provider.Telephony
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unilim.iut.truckers.controleur.LogcatControleur

class ServiceDuReceveurDeSMS(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val smsReceiver = ReceveurDeSMS()
    private val logcatControleur = LogcatControleur()

    override fun doWork(): Result {
        val preferencesDePartage = applicationContext.getSharedPreferences("sms_service_prefs", Context.MODE_PRIVATE)
        val receveurEstInscrit = preferencesDePartage.getBoolean("is_receiver_registered", false)

        if (!receveurEstInscrit) {
            val filtre = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            applicationContext.registerReceiver(smsReceiver, filtre)

            preferencesDePartage.edit().putBoolean("is_receiver_registered", true).apply()
            Log.d("TruckerService", "Service actif")
            logcatControleur.ecrireDansFichierLog("Service actif")
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
