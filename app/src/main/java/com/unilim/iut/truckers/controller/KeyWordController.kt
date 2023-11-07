package com.unilim.iut.truckers.controller

import android.content.Context
import android.util.Log

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
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementMotsCles(context: Context?): MutableList<String> {
        val jsonObject = jsonController.charger(context, "MotsCles.json")
        val motsCles = mutableListOf<String>()

        if (jsonObject.has("mots_cles")) {
            val jsonArray = jsonObject.getJSONArray("mots_cles")

            for (i in 0 until jsonArray.length()) {
                val motCle = jsonArray.getString(i)
                motsCles.add(motCle)
            }
        }

        return motsCles
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

        for (motCle in listMotsCles.toString()) {
            if (message.contains(motCle)) {
                Log.d("TruckerService", "Message contenant un mot-clé")
                return true
            }
        }

        Log.d("TruckerService", "Message ne contenant pas de mot-clé")
        return false
    }
}