package com.unilim.iut.truckers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.unilim.iut.truckers.controller.MessageController
import com.unilim.iut.truckers.model.PhoneNumber
import com.unilim.iut.truckers.controller.WhiteListController
import com.unilim.iut.truckers.model.Message

class SmsReceiver : BroadcastReceiver() {

    private val controlleurListeBlanche = WhiteListController();
    private val controllerMessage = MessageController();

    override fun onReceive(contexte: Context?, intention: Intent?) {
        if (intention != null) {
            if (intention.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intention)
                for (message in messages) {
                    val numeroEmetteur = message.originatingAddress?.let { PhoneNumber(it) }
                    val corpsMessage = message.messageBody

                    val listeBlanche = controlleurListeBlanche.chargementListeBlanche(contexte, false)
                    val numeroAdmin = controlleurListeBlanche.chargementListeBlanche(contexte, true)

                    if (controlleurListeBlanche.numeroAdministrateur(numeroEmetteur, numeroAdmin)) {
                        Log.d("SMSReceiver", "SMS administrateur autorisé")
                        controllerMessage.ajoutMessageDansJsonBonMessage(contexte, Message(numeroEmetteur!!, corpsMessage, message.timestampMillis.toString()))
                    } else {
                        if (controlleurListeBlanche.numeroDansLaListeBlanche(numeroEmetteur, listeBlanche.toSet())) {
                            Log.d("SMSReceiver", "SMS autorisé")
                            controllerMessage.ajoutMessageDansJsonBonMessage(contexte, Message(numeroEmetteur!!, corpsMessage, message.timestampMillis.toString()))
                        } else {
                            Log.d("SMSReceiver", "SMS non autorisé")
                            controllerMessage.ajoutMessageDansMauvaisJsonMessage(contexte, Message(numeroEmetteur!!, corpsMessage, message.timestampMillis.toString()))
                        }
                    }


                }
            }
        }
    }
}
