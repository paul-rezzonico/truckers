package com.unilim.iut.truckers.controller

import android.content.Context
import com.unilim.iut.truckers.model.PhoneNumber
import java.io.File

class DefaultController {

    private val controlleurJson = JsonController()

    /**
     * Cette fonction permet de vérifier si un fichier JSON existe.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @return Cette fonction retourne un booléen.
     */
    fun verificationDefaultJson(context: Context?): Boolean {
        val fichier = File(context?.filesDir, "defaut.json")
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
    fun chargementJson(context: Context?): String {
        return controlleurJson.charger(context, "defaut.json").toString()
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementMotsClesDefaut(context: Context?): MutableList<String> {
        val jsonObject = controlleurJson.charger(context, "defaut.json")
        val liste = mutableListOf<String>()

        val jsonArray = jsonObject.getJSONArray("mots_cles")

        for (i in 0 until jsonArray.length()) {
            val motCle = jsonArray.getString(i)
            liste.add(motCle)
        }

        return liste
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementListeBlancheDefaut(context: Context?, nomTableauJson: String): MutableList<PhoneNumber> {
        val jsonObject = controlleurJson.charger(context, "defaut.json")
        val liste = mutableListOf<PhoneNumber>()

        val jsonArray = jsonObject.getJSONArray(nomTableauJson)

        for (i in 0 until jsonArray.length()) {
            val motCle = jsonArray.getString(i)
            liste.add(PhoneNumber(motCle))
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
        controlleurJson.supressionJSON(contexte, "defaut.json")
    }
}