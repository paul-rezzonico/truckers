package com.unilim.iut.truckers.controleur

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import com.unilim.iut.truckers.api.ApiManager
import com.unilim.iut.truckers.commande.*
import com.unilim.iut.truckers.modele.JsonData
import com.unilim.iut.truckers.modele.Message
import com.unilim.iut.truckers.modele.NumeroTelephone
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageControleur {

    private val controleurJson = JsonControleur()
    private val controleurCommande = CommandeControleur()
    private val jackson = ObjectMapper().registerModule(KotlinModule())

    private val motCleAjout = "Ajout"
    private val motCleSuppression = "Suppression"
    private val motCleChangement = "Changement"

    private fun creationNomFichierJSON(prefixe: String): String {
        val dateDuJour = SimpleDateFormat("dd-M-yyyy", Locale.FRANCE).format(Date())
        return prefixe + "_" + "$dateDuJour.json"
    }

    private fun creationJsonMessage(contexte: Context?, prefixe: String) {
        controleurJson.creationFichierJSON(JsonData(contexte, creationNomFichierJSON(prefixe), "messages", null, null))
    }

    private fun ajoutMessageDansJsonMessage(contexte: Context?, prefixe: String, message: Message) {
        controleurJson.sauvegarderDonneesDansJSON(JsonData(contexte, creationNomFichierJSON(prefixe), "messages", message, null))
    }

    private fun avoirMessagesDansJsonMessage(contexte: Context?, prefixe: String): MutableList<Message> {
        val objetJson = controleurJson.chargerDonneesDuJSON(JsonData(contexte, creationNomFichierJSON(prefixe), null, null, null))
        val liste = mutableListOf<Message>()

        if (objetJson.toString() == "{}") {
            return liste
        }

        val tableauJson = objetJson.getJSONArray("messages")

        for (i in 0 until tableauJson.length()) {
            val message = tableauJson.getString(i)
            liste.add(jackson.readValue(message, Message::class.java))
        }

        return liste
    }

    fun creationJsonBonMessage(contexte: Context?) {
        creationJsonMessage(contexte, "MessageValide")
    }

    fun ajoutMessageDansBonMessage(contexte: Context?, message: Message) {
        ajoutMessageDansJsonMessage(contexte, "MessageValide", message)
    }

    fun avoirMessagesDansBonJsonMessage(contexte: Context?): MutableList<Message> {
        return avoirMessagesDansJsonMessage(contexte, "MessageValide")
    }

    fun creationJsonMauvaisMessage(contexte: Context?) {
        creationJsonMessage(contexte, "MessageInvalide")
    }

    fun ajoutMessageDansMauvaisJsonMessage(contexte: Context?, message: Message) {
        ajoutMessageDansJsonMessage(contexte, "MessageInvalide", message)
    }

    fun avoirMessagesDansMauvaisJsonMessage(contexte: Context?): MutableList<Message> {
        return avoirMessagesDansJsonMessage(contexte, "MessageInvalide")
    }

    fun actionMessage(contexte: Context?, nouveauMessage: Message) {
        val lignes = nouveauMessage.message.lines().drop(1)

        for (ligne in lignes) {
            val action = ligne.split(" ")[0]
            val cle = ligne.split(" ")[1]
            val value = ligne.split(" ")[2]

            when (action) {
                motCleAjout -> {
                    when (cle) {
                        "mot-clé" -> controleurCommande.executerCommande(AjoutMotCleCommande(contexte, value))
                        "numéro" -> controleurCommande.executerCommande(AjoutNumeroListeBlancheCommande(contexte, NumeroTelephone(value)))
                        "administrateur" -> controleurCommande.executerCommande(AjoutNumeroAdminCommande(contexte, NumeroTelephone(value)))
                    }
                }
                motCleSuppression -> {
                    when (cle) {
                        "mot-clé" -> controleurCommande.executerCommande(SupprimerMotCleCommande(contexte, value))
                        "numéro" -> controleurCommande.executerCommande(SupprimerNumeroListeBlancheCommande(contexte, NumeroTelephone(value)))
                        "administrateur" -> controleurCommande.executerCommande(SupprimerNumeroAdminCommande(contexte, NumeroTelephone(value)))
                    }
                }
                motCleChangement -> controleurCommande.executerCommande(ChangerIntervalleSynchronisationCommande(contexte, value))
                else -> {
                    Log.d("TruckerService", "Action non reconnue")
                }
            }
        }
    }

    fun supprimerMessagesApresApi(
        contexte: Context,
        androidId: String,
        messages: List<Message>,
        nombreMessageEnregistre: Int
    ) {
        val messagesApiValides = ApiManager(contexte).recevoirMessages("messages/${androidId}")
        val messagesApiInvalides = ApiManager(contexte).recevoirMessages("messages_err/${androidId}")

        val jsonMessagesApiValides = Gson().toJson(messagesApiValides)
        val jsonMessagesApiInvalides = Gson().toJson(messagesApiInvalides)

        for (message in messages) {
            if (jsonMessagesApiValides.contains(message.id)) {
                controleurCommande.executerCommande(SupprimerMessageCommande(contexte, message, creationNomFichierJSON("MessageValide"), nombreMessageEnregistre))
            } else if (jsonMessagesApiInvalides.contains(message.id)) {
                controleurCommande.executerCommande(SupprimerMessageCommande(contexte, message, creationNomFichierJSON("MessageInvalide"), nombreMessageEnregistre))
            }
        }
    }
}
