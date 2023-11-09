package com.unilim.iut.truckers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.unilim.iut.truckers.controller.KeyWordController
import com.unilim.iut.truckers.controller.MessageController
import com.unilim.iut.truckers.controller.WhiteListController
import com.unilim.iut.truckers.model.PhoneNumber
import com.unilim.iut.truckers.model.Message

class SmsReceiver : BroadcastReceiver() {

    private val controlleurListeBlanche = WhiteListController();
    private val controllerMessage = MessageController();
    private val controllerMotCle = KeyWordController();

    override fun onReceive(contexte: Context?, intention: Intent?) {
        if (intention != null) {

            if (intention.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intention)

                for (message in messages) {
                    Log.d("TruckerService", "--------------------------------------")
                    val numeroEmetteur = message.originatingAddress?.let { PhoneNumber(it) }
                    val corpsMessage = message.messageBody

                    val listeBlanche = controlleurListeBlanche.chargementListeBlanche(contexte, false)
                    val numeroAdmin = controlleurListeBlanche.chargementListeBlanche(contexte, true)

                    if (numeroEmetteur != null) {
                        when(numeroEmetteur.phoneNumber) {
                            in listeBlanche.toString() -> {
                                Log.d("TruckerService", "Message de la liste blanche")
                                if (controllerMotCle.verificationMotsCles(contexte, corpsMessage)) {
                                    controllerMessage.ajoutMessageDansJsonBonMessage(contexte, Message(numeroEmetteur, corpsMessage, message.timestampMillis.toString()))
                                } else {
                                    controllerMessage.ajoutMessageDansMauvaisJsonMessage(contexte, Message(numeroEmetteur, corpsMessage, message.timestampMillis.toString()))
                                }
                            }

                            in numeroAdmin.toString() -> {
                                Log.d("TruckerService", "Message de l'administrateur")
                                if (controllerMotCle.verificationMotsClesAdmin(contexte, corpsMessage)) {
                                    controllerMessage.ajoutMessageDansJsonBonMessage(contexte, Message(numeroEmetteur, corpsMessage, message.timestampMillis.toString()))
                                    controllerMessage.actionMessage(contexte, Message(numeroEmetteur, corpsMessage, message.timestampMillis.toString()))
                                } else {
                                    controllerMessage.ajoutMessageDansMauvaisJsonMessage(contexte, Message(numeroEmetteur, corpsMessage, message.timestampMillis.toString()))
                                }
                            }

                            else -> {
                                Log.d("TruckerService", "Message Invalide")
                            }
                        }
                    }
                }


            }
        }
    }
}

