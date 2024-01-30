package com.unilim.iut.truckers.controleur

import android.content.Context
import android.os.Environment
import com.unilim.iut.truckers.modele.JsonData
import com.unilim.iut.truckers.modele.NumeroTelephone
import java.io.File

class DefautControleur {

    private val controleurJson = ChargeurDeStockageExterne()
    private val nomFichierDefaut = "defaut.json"

    /**
     * Cette fonction permet de vérifier si un fichier JSON existe.
     *
     * @return Cette fonction retourne un booléen.
     */
    fun verificationDefaultJson(): Boolean {
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fichier = File(downloadsPath, nomFichierDefaut)

        return fichier.exists()
    }

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun chargementJson(contexte: Context?): String {
        return controleurJson.chargerDonneesDuJSON(JsonData(contexte, nomFichierDefaut, null, null, null)).toString()
    }

    /**
     * Cette fonction permet de charger une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de String mot-clés.
     */
    fun chargementMotsClesDefaut(contexte: Context?): MutableList<String> {
        val objetJson = controleurJson.chargerDonneesDuJSON(JsonData(contexte, nomFichierDefaut, null, null, null))
        val liste = mutableListOf<String>()

        val tableauJSON = objetJson.getJSONArray("mots_cles")

        for (i in 0 until tableauJSON.length()) {
            val motCle = tableauJSON.getString(i)
            liste.add(motCle.toString())
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
        val objetJson = controleurJson.chargerDonneesDuJSON(JsonData(contexte, nomFichierDefaut, null, null, null))
        val liste = mutableListOf<NumeroTelephone>()

        val tableauJson = objetJson.getJSONArray(nomTableauJson)

        for (i in 0 until tableauJson.length()) {
            val numero = tableauJson.getString(i)
            liste.add(NumeroTelephone(numero.toString()))
        }

        return liste
    }

}