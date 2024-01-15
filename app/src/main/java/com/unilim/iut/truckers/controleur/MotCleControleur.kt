package com.unilim.iut.truckers.controleur

import android.content.Context
import android.util.Log

class MotCleControleur {

    private val jsonControleur = JsonControleur()
    private val controleurLogcat = LogcatControleur()

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJSONMotCle(contexte: Context?) {
        jsonControleur.creationFichierJSON(contexte, "MotsCles.json", "mots_cles")
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementMotsCles(context: Context?): MutableList<String> {
        val jsonObject = jsonControleur.chargerDonneesDuJSON(context, "MotsCles.json")
        val motsCles = mutableListOf<String>()

        if (jsonObject.has("mots_cles")) {
            val tableauJSON = jsonObject.getJSONArray("mots_cles")

            for (i in 0 until tableauJSON.length()) {
                val motCle = tableauJSON.getString(i)
                motsCles.add(motCle.toString())
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
        val listeMotsCles = chargementMotsCles(contexte)

        for (motCle in listeMotsCles) {
            if (message.contains(motCle.replace("\"", ""))) {
                Log.d("TruckerService", "Message contenant un mot-clé")
                controleurLogcat.ecrireDansFichierLog("Message contenant un mot-clé : $motCle")
                return true
            }
        }

        Log.d("TruckerService", "Message ne contenant pas de mot-clé")
        controleurLogcat.ecrireDansFichierLog("Message ne contenant pas de mot-clé")
        return false
    }

    /**
     * Cette fonction permet de vérifier si un message contient un mot-clé.
     *
     * @param message Ce paramètre est le message à vérifier.
     * @return Cette fonction retourne un booléen.
     */
    fun verificationMotsClesAdmin(message: String): Boolean {
        val motCleAdmin = "CONFIG"

        if (message.contains(motCleAdmin)) {
            Log.d("TruckerService", "Message contenant un mot-clé Admin")
            controleurLogcat.ecrireDansFichierLog("Message contenant un mot-clé Admin : $motCleAdmin")
            return true
        }

        Log.d("TruckerService", "Message ne contenant pas de mot-clé Admin")
        controleurLogcat.ecrireDansFichierLog("Message ne contenant pas de mot-clé Admin")
        return false
    }
}