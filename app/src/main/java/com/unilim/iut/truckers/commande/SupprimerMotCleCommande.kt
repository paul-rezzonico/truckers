package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.controleur.LogcatControleur

class SupprimerMotCleCommande(
    override var context: Context?,
    override var donnee: Any?
) : Commande() {

    private val logcatControleur = LogcatControleur()

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            jsonControleur.supprimer(
                context, "MotsCles.json", "mots_cles",
                it
            )
        }

        if (effectue == false) {
            Log.d("TruckerService", "Suppression d'un mot clé impossible car déjà présent: $donnee")
            logcatControleur.ecrireDansFichierLog("Suppression d'un mot clé impossible car déjà présent: $donnee")
            return false
        }

        Log.d("TruckerService", "Suppression d'un mot clé: $donnee")
        logcatControleur.ecrireDansFichierLog("Suppression d'un mot clé: $donnee")
        return true
    }
}