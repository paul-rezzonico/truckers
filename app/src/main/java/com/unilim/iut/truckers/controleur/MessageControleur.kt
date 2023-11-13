package com.unilim.iut.truckers.controleur

import android.content.Context
import com.unilim.iut.truckers.commande.AjoutNumeroAdminCommande
import com.unilim.iut.truckers.commande.AjoutMotCleCommande
import com.unilim.iut.truckers.commande.AjoutNumeroListeBlancheCommande
import com.unilim.iut.truckers.commande.SupprimerNumeroAdminCommande
import com.unilim.iut.truckers.commande.SupprimerMotCleCommande
import com.unilim.iut.truckers.commande.SupprimerNumeroListeBlancheCommande
import com.unilim.iut.truckers.modele.Message
import com.unilim.iut.truckers.modele.NumeroTelephone
import java.text.SimpleDateFormat
import java.util.Date

class MessageControleur {

    private val controleurJson = JsonControleur()
    private val controleurCommande = CommandeControleur()

    fun creationNomFichierJSON(prefixe: String): String {
        val dateDuJour = SimpleDateFormat("dd-M-yyyy").format(Date())
        return "$prefixe" + "_" + "$dateDuJour.json"
    }

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste d'objet Message.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJsonBonMessage(contexte: Context?) {
        controleurJson.creationJSON(contexte, creationNomFichierJSON("MessageValide"), "messages")
    }

    /**
     * Cette fonction permet d'ajouter un message dans le fichier JSON contenant une liste d'objet Message qui sont ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutMessageDansJsonBonMessage(contexte: Context?, message: Message) {
        controleurJson.sauvegarder(contexte, creationNomFichierJSON("MessageValide"), "messages", message)
    }

    /**
     * Cette fonction permet de supprimer le fichier JSON contenant une liste d'objet Message qui sont ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun suppressionJsonBonMessage(contexte: Context?) {
        controleurJson.supressionJSON(contexte, creationNomFichierJSON("MessageValide"))
    }

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJsonMauvaisMessage(contexte: Context?) {
        controleurJson.creationJSON(contexte, creationNomFichierJSON("MessageInvalide"), "messages")
    }

    /**
     * Cette fonction permet d'ajouter un message dans le fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutMessageDansMauvaisJsonMessage(contexte: Context?, message: Message) {
        controleurJson.sauvegarder(contexte, creationNomFichierJSON("MessageInvalide"), "messages", message)
    }

    /**
     * Cette fonction permet de supprimer le fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun suppressionJsonMauvaisMessage(contexte: Context?) {
        controleurJson.supressionJSON(contexte, creationNomFichierJSON("MessageInvalide"))
    }

    /**
     * Cette fonction permet de mettre en place une commande en fonction du message de l'admin
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste d'objet Message.
     */
    fun actionMessage(contexte: Context?, nouveauMessage: Message) {
        val motCleAjout = "Ajout"
        val motCleSuppression = "Suppression"
        val lignes = nouveauMessage.message.lines().drop(1)

        for (ligne in lignes) {
            val action = ligne.split(" ")[0]
            val cle = ligne.split(" ")[1]
            val value = ligne.split(" ")[2]

            if (action == motCleAjout) {
                when (cle) {
                    "mot-clé" -> {
                        controleurCommande.executerCommande(AjoutMotCleCommande(contexte, value))
                    }
                    "numéro" -> {
                        controleurCommande.executerCommande(AjoutNumeroListeBlancheCommande(contexte, NumeroTelephone(value)))
                    }
                    "administrateur" -> {
                        controleurCommande.executerCommande(AjoutNumeroAdminCommande(contexte, NumeroTelephone(value)))
                    }
                }
            } else if (action == motCleSuppression) {
                when (cle) {
                    "mot-clé" -> {
                        controleurCommande.executerCommande(SupprimerMotCleCommande(contexte, value))
                    }
                    "numéro" -> {
                        controleurCommande.executerCommande(SupprimerNumeroListeBlancheCommande(contexte, NumeroTelephone(value)))
                    }
                    "administrateur" -> {
                        controleurCommande.executerCommande(SupprimerNumeroAdminCommande(contexte, NumeroTelephone(value)))
                    }
                }
            }
        }
    }
}