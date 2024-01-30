package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.controleur.LogcatControleur
import com.unilim.iut.truckers.modele.JsonData

class SupprimerMessageCommande (
    override var context: Context?,
    override var donnee: Any?,
    private val cheminFichier: String,
    private val nombreMessageEnregistre: Int
) : Commande() {

    private val logcatControleur = LogcatControleur()

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            jsonControleur.supprimerDonneesDansJSON(JsonData(context, cheminFichier, "messages", it, nombreMessageEnregistre))
        }
        if (effectue == false) {
            Log.d("TruckerService", "Suppression d'un message dans le fichier $cheminFichier impossible: $donnee")
            logcatControleur.ecrireDansFichierLog("Suppression d'un message dans le fichier $cheminFichier impossible: $donnee")
            return false
        }
        Log.d("TruckerService", "Suppression d'un message dans le fichier $cheminFichier: $donnee")
        logcatControleur.ecrireDansFichierLog("Suppression d'un message dans le fichier $cheminFichier: $donnee")
        return true
    }
}