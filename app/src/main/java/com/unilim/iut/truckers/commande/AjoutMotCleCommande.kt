package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.controleur.LogcatControleur

class AjoutMotCleCommande(
    override var context: Context?,
    override var donnee: Any?
) : Commande() {

    private val logcatControleur = LogcatControleur()

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            jsonControleur.sauvegarder(context, "MotsCles.json", "mots_cles",
                it
            )
        }
        if (effectue == false) {
            Log.d("TruckerService", "Ajout d'un mot clé impossible car déjà présent: $donnee")
            logcatControleur.ecrireDansFichierLog("Ajout d'un mot clé impossible car déjà présent: $donnee")
            return false
        }
        Log.d("TruckerService", "Ajout d'un mot clé: $donnee")
        logcatControleur.ecrireDansFichierLog("Ajout d'un mot clé: $donnee")
        return true
    }
}