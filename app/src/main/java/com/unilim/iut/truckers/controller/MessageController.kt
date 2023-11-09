package com.unilim.iut.truckers.controller

import android.content.Context
import com.unilim.iut.truckers.MainActivity
import com.unilim.iut.truckers.command.AddAdminNumberCommand
import com.unilim.iut.truckers.command.AddKeyWordCommand
import com.unilim.iut.truckers.command.AddWhiteListNumberCommand
import com.unilim.iut.truckers.command.DeleteAdminNumberCommand
import com.unilim.iut.truckers.command.DeleteKeyWordCommand
import com.unilim.iut.truckers.command.DeleteWhiteListNumberCommand
import com.unilim.iut.truckers.model.Message
import com.unilim.iut.truckers.model.PhoneNumber
import java.text.SimpleDateFormat
import java.util.Date

class MessageController {

    private val controlleurJson = JsonController()
    private val controlleurCommande = CommandController()

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
        controlleurJson.creationJSON(contexte, creationNomFichierJSON("MessageValide"), "messages")
    }

    /**
     * Cette fonction permet d'ajouter un message dans le fichier JSON contenant une liste d'objet Message qui sont ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutMessageDansJsonBonMessage(contexte: Context?, message: Message) {
        controlleurJson.sauvegarder(contexte, creationNomFichierJSON("MessageValide"), "messages", message)
    }

    /**
     * Cette fonction permet de supprimer le fichier JSON contenant une liste d'objet Message qui sont ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun suppressionJsonBonMessage(contexte: Context?) {
        controlleurJson.supressionJSON(contexte, creationNomFichierJSON("MessageValide"))
    }

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJsonMauvaisMessage(contexte: Context?) {
        controlleurJson.creationJSON(contexte, creationNomFichierJSON("MessageInvalide"), "messages")
    }

    /**
     * Cette fonction permet d'ajouter un message dans le fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutMessageDansMauvaisJsonMessage(contexte: Context?, message: Message) {
        controlleurJson.sauvegarder(contexte, creationNomFichierJSON("MessageInvalide"), "messages", message)
    }

    /**
     * Cette fonction permet de supprimer le fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun suppressionJsonMauvaisMessage(contexte: Context?) {
        controlleurJson.supressionJSON(contexte, creationNomFichierJSON("MessageInvalide"))
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
                        controlleurCommande.executerCommande(AddKeyWordCommand(contexte, value))
                    }
                    "numéro" -> {
                        controlleurCommande.executerCommande(AddWhiteListNumberCommand(contexte, PhoneNumber(value)))
                    }
                    "administrateur" -> {
                        controlleurCommande.executerCommande(AddAdminNumberCommand(contexte, PhoneNumber(value)))
                    }
                }
            } else if (action == motCleSuppression) {
                when (cle) {
                    "mot-clé" -> {
                        controlleurCommande.executerCommande(DeleteKeyWordCommand(contexte, value))
                    }
                    "numéro" -> {
                        controlleurCommande.executerCommande(DeleteWhiteListNumberCommand(contexte, PhoneNumber(value)))
                    }
                    "administrateur" -> {
                        controlleurCommande.executerCommande(DeleteAdminNumberCommand(contexte, PhoneNumber(value)))
                    }
                }
            }
        }
    }
}