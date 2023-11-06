package com.unilim.iut.truckers.controller

import android.content.Context
import android.util.Log
import org.json.JSONObject

class KeyWordController {

    private val jsonController = JsonController();

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJSONMotCle(contexte: Context?) {
        jsonController.creationJSON(contexte, "MotsCles.json", "mots_cles")
        jsonController.ajoutDonneesJSON(contexte, "MotsCles.json", "mots_cles", listOf("RENDEZ-VOUS", "RDV", "LIVRAISON", "LIV", "URGENT", "URG", "INFORMATION", "INF"))
    }

    /**
     * Cette fonction permet de charger le contenu d'un fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne le contenu du fichier JSON.
     */
    fun chargementJson(context: Context?): JSONObject {
        return jsonController.chargementJSON(context, "MotsCles.json")
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementMotsCles(context: Context?): MutableList<String> {
        val jsonObject = chargementJson(context)
        val motsCles = mutableListOf<String>()

        val jsonArray = jsonObject.getJSONArray("mots_cles")

        for (i in 0 until jsonArray.length()) {
            val motCle = jsonArray.getString(i)
            motsCles.add(motCle)
        }

        return motsCles
    }

    /**
     * Cette fonction permet d'ajouter un mot-clé dans le fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param motCle Ce paramètre est le mot-clé à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun suppressionJSONMotCle(contexte: Context?) {
        jsonController.supressionJSON(contexte, "MotsCles.json")
    }

    /**
     * Cette fonction permet d'ajouter des mots-clés dans le fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param motsCles Ce paramètre est les mots-clés à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutMotsCles(contexte: Context?, motsCles: List<String>) {
        jsonController.ajoutDonneesJSON(contexte, "MotsCles.json", "mots_cles", motsCles)
    }

    /**
     * Cette fonction permet de supprimer des mots-clés dans le fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param motsCles Ce paramètre est les mots-clés à supprimer du fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun supressionMotsCles(contexte: Context?, motsCles: List<String>) {
        jsonController.supressionDonneesJSON(contexte, "MotsCles.json", "mots_cles", motsCles)
    }

    /**
     * Cette fonction permet de vérifier si un message contient un mot-clé.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est le message à vérifier.
     * @return Cette fonction retourne un booléen.
     */
    fun verificationMotsCles(contexte: Context?, message: String): Boolean {
        val listMotsCles = chargementMotsCles(contexte)

        for (motCle in listMotsCles) {
            if (message.contains(motCle)) {
                Log.d("SMSReceiver", "Message contenant un mot-clé")
                return true
            }
        }

        Log.d("SMSReceiver", "Message ne contenant pas de mot-clé")
        return false
    }
}