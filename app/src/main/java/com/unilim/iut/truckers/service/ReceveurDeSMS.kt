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
        if (intentionSmsRecu(intention)) {
            traiterSmsEntrant(contexte, intention)
        }
    }

    private fun intentionSmsRecu(intention: Intent?): Boolean {
        return intention?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION
    }

    private fun traiterSmsEntrant(contexte: Context?, intention: Intent?) {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intention)
        for (message in messages) {
            gererMessageRecu(contexte, message)
        }
    }

    private fun gererMessageRecu(contexte: Context?, message: SmsMessage) {
        val numeroEmetteur = recupererNumeroEmetteur(message)
        if (numeroEmetteur != null) {
            Log.d("TruckerService", "--------------------------------------------------")
            when {
                estDansListeBlanche(contexte, numeroEmetteur) -> gererMessageListeBlanche(contexte, numeroEmetteur, message)
                estUnAdmin(contexte, numeroEmetteur) -> gererMessageAdmin(contexte, numeroEmetteur, message)
                else -> gererMessageInvalide()
            }
        }
    }

    private fun recupererNumeroEmetteur(message: SmsMessage): NumeroTelephone? {
        return try {
            message.originatingAddress?.let { NumeroTelephone(it) }
        } catch (e: Exception) {
            Log.d("TruckerService", e.message.toString())
            controleurLogcat.ecrireDansFichierLog(e.message.toString())
            null
        }
    }

    private fun estDansListeBlanche(contexte: Context?, numeroEmetteur: NumeroTelephone): Boolean {
        val listeBlanche = controleurListeBlanche.chargementListeBlanche(contexte, false)
        return numeroEmetteur.numeroTelephone in listeBlanche.toString()
    }

    private fun estUnAdmin(contexte: Context?, numeroEmetteur: NumeroTelephone): Boolean {
        val numeroAdmin = controleurListeBlanche.chargementListeBlanche(contexte, true)
        return numeroEmetteur.numeroTelephone in numeroAdmin.toString()
    }

    private fun gererMessageListeBlanche(contexte: Context?, numeroEmetteur: NumeroTelephone, message: SmsMessage) {
        Log.d("TruckerService", "Message de la liste blanche")
        controleurLogcat.ecrireDansFichierLog("Message de la liste blanche")

        if (controleurMotCle.verificationMotsCles(contexte, message.messageBody)) {
            controleurMessage.ajoutMessageDansBonMessage(contexte, Message(numeroEmetteur.numeroTelephone, message.messageBody, message.timestampMillis.toString()))
        } else {
            controleurMessage.ajoutMessageDansMauvaisJsonMessage(contexte, Message(numeroEmetteur.numeroTelephone, message.messageBody, message.timestampMillis.toString()))
        }
    }

    private fun gererMessageAdmin(contexte: Context?, numeroEmetteur: NumeroTelephone, message: SmsMessage) {
        Log.d("TruckerService", "Message de l'administrateur")
        controleurLogcat.ecrireDansFichierLog("Message de l'administrateur")

        if (controleurMotCle.verificationMotsClesAdmin(message.messageBody)) {
            controleurMessage.ajoutMessageDansBonMessage(contexte, Message(numeroEmetteur.numeroTelephone, message.messageBody, message.timestampMillis.toString()))
            controleurMessage.actionMessage(contexte, Message(numeroEmetteur.numeroTelephone, message.messageBody, message.timestampMillis.toString()))
        } else {
            controleurMessage.ajoutMessageDansMauvaisJsonMessage(contexte, Message(numeroEmetteur.numeroTelephone, message.messageBody, message.timestampMillis.toString()))
        }
    }

    private fun gererMessageInvalide() {
        Log.d("TruckerService", "Message Invalide")
        controleurLogcat.ecrireDansFichierLog("Message Invalide")
    }
}
