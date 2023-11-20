package com.unilim.iut.truckers.controleur

import android.content.Context
import com.unilim.iut.truckers.modele.NumeroTelephone
import java.io.File

class DefautControleur {

    private val controleurJson = JsonControleur()

    /**
     * Cette fonction permet de vérifier si un fichier JSON existe.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @return Cette fonction retourne un booléen.
     */
    fun verificationDefaultJson(contexte: Context?): Boolean {
        val fichier = File(contexte?.filesDir, "defaut.json")
        if (!fichier.exists()) {
            return false
        }
        return true
    }

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun chargementJson(contexte: Context?): String {
        return controleurJson.charger(contexte, "defaut.json").toString()
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementMotsClesDefaut(contexte: Context?): MutableList<String> {
        val objetJson = controleurJson.charger(contexte, "defaut.json")
        val liste = mutableListOf<String>()

        val tableauJSON = objetJson.getJSONArray("mots_cles")

        for (i in 0 until tableauJSON.length()) {
            val motCle = tableauJSON.getString(i)
            liste.add(motCle)
        }

        return liste
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementListeBlancheDefaut(contexte: Context?, nomTableauJson: String): MutableList<NumeroTelephone> {
        val objetJson = controleurJson.charger(contexte, "defaut.json")
        val liste = mutableListOf<NumeroTelephone>()

        val tableauJson = objetJson.getJSONArray(nomTableauJson)

        for (i in 0 until tableauJson.length()) {
            val motCle = tableauJson.getString(i)
            liste.add(NumeroTelephone(motCle))
        }

        return liste
    }

    /**
     * Cette fonction permet d'ajouter un mot-clé dans le fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param motCle Ce paramètre est le mot-clé à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun suppressionJSONDefaut(contexte: Context?) {
        controleurJson.supressionJSON(contexte, "defaut.json")
    }
}