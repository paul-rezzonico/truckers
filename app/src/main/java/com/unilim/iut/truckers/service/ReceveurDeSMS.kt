package com.unilim.iut.truckers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.unilim.iut.truckers.controleur.MotCleControleur
import com.unilim.iut.truckers.controleur.MessageControleur
import com.unilim.iut.truckers.controleur.ListeBlancheControleur
import com.unilim.iut.truckers.controleur.LogcatControleur
import com.unilim.iut.truckers.modele.NumeroTelephone
import com.unilim.iut.truckers.modele.Message

class ReceveurDeSMS : BroadcastReceiver() {

    private val controleurListeBlanche = ListeBlancheControleur()
    private val controleurMessage = MessageControleur()
    private val controleurMotCle = MotCleControleur()
    private val controleurLogcat = LogcatControleur()

    override fun onReceive(contexte: Context?, intention: Intent?) {
        if (isSmsReceivedIntent(intention)) {
            processReceivedSms(contexte, intention)
        }
    }

    private fun isSmsReceivedIntent(intent: Intent?): Boolean {
        return intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION
    }

    private fun processReceivedSms(contexte: Context?, intention: Intent?) {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intention)
        for (message in messages) {
            handleReceivedMessage(contexte, message)
        }
    }

    private fun handleReceivedMessage(contexte: Context?, message: SmsMessage) {
        val numeroEmetteur = getNumeroEmetteur(message)
        if (numeroEmetteur != null) {
            Log.d("TruckerService", "--------------------------------------------------")
            when {
                isMessageFromListeBlanche(contexte, numeroEmetteur) -> handleListeBlancheMessage(contexte, numeroEmetteur, message)
                isMessageFromAdmin(contexte, numeroEmetteur) -> handleAdminMessage(contexte, numeroEmetteur, message)
                else -> handleInvalidMessage()
            }
        }
    }

    private fun getNumeroEmetteur(message: SmsMessage): NumeroTelephone? {
        return try {
            message.originatingAddress?.let { NumeroTelephone(it) }
        } catch (e: Exception) {
            Log.d("TruckerService", e.message.toString())
            controleurLogcat.ecrireDansFichierLog(e.message.toString())
            null
        }
    }

    private fun isMessageFromListeBlanche(contexte: Context?, numeroEmetteur: NumeroTelephone): Boolean {
        val listeBlanche = controleurListeBlanche.chargementListeBlanche(contexte, false)
        return numeroEmetteur.numeroTelephone in listeBlanche.toString()
    }

    private fun isMessageFromAdmin(contexte: Context?, numeroEmetteur: NumeroTelephone): Boolean {
        val numeroAdmin = controleurListeBlanche.chargementListeBlanche(contexte, true)
        return numeroEmetteur.numeroTelephone in numeroAdmin.toString()
    }

    private fun handleListeBlancheMessage(contexte: Context?, numeroEmetteur: NumeroTelephone, message: SmsMessage) {
        Log.d("TruckerService", "Message de la liste blanche")
        controleurLogcat.ecrireDansFichierLog("Message de la liste blanche")

        if (controleurMotCle.verificationMotsCles(contexte, message.messageBody)) {
            controleurMessage.ajoutMessageDansJsonBonMessage(contexte, Message(numeroEmetteur, message.messageBody, message.timestampMillis.toString()))
        } else {
            controleurMessage.ajoutMessageDansMauvaisJsonMessage(contexte, Message(numeroEmetteur, message.messageBody, message.timestampMillis.toString()))
        }
    }

    private fun handleAdminMessage(contexte: Context?, numeroEmetteur: NumeroTelephone, message: SmsMessage) {
        Log.d("TruckerService", "Message de l'administrateur")
        controleurLogcat.ecrireDansFichierLog("Message de l'administrateur")

        if (controleurMotCle.verificationMotsClesAdmin(message.messageBody)) {
            controleurMessage.ajoutMessageDansJsonBonMessage(contexte, Message(numeroEmetteur, message.messageBody, message.timestampMillis.toString()))
            controleurMessage.actionMessage(contexte, Message(numeroEmetteur, message.messageBody, message.timestampMillis.toString()))
        } else {
            controleurMessage.ajoutMessageDansMauvaisJsonMessage(contexte, Message(numeroEmetteur, message.messageBody, message.timestampMillis.toString()))
        }
    }

    private fun handleInvalidMessage() {
        Log.d("TruckerService", "Message Invalide")
        controleurLogcat.ecrireDansFichierLog("Message Invalide")
    }
}
