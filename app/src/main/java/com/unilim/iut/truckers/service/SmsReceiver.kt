package com.unilim.iut.truckers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.unilim.iut.truckers.model.PhoneNumber
import com.unilim.iut.truckers.controller.WhiteListController

class SmsReceiver : BroadcastReceiver() {

    private val whiteListController = WhiteListController();

    override fun onReceive(contexte: Context?, intention: Intent?) {
        if (intention != null) {
            if (intention.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intention)
                for (message in messages) {
                    val numeroEmetteur = message.originatingAddress?.let { PhoneNumber(it) }
                    val corpsMessage = message.messageBody

                    val listeBlanche = whiteListController.chargementListeBlanche(contexte)

                    if (whiteListController.numeroDansLaListeBlanche(numeroEmetteur, listeBlanche.toSet())) {
                        Log.d("SMSReceiver", "SMS autorisé")
                        Log.d("SMSReceiver", "SMS reçu de $numeroEmetteur : $corpsMessage")
                    } else {
                        Log.d("SMSReceiver", "SMS non autorisé")
                    }
                }
            }
        }
    }
}
