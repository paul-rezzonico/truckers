package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.controleur.LogcatControleur

class AjoutNumeroAdminCommande(
    override var context: Context?,
    override var donnee: Any?
): Commande() {

    private val logcatControleur = LogcatControleur()

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            jsonControleur.sauvegarder(context, "ListeBlanche.json", "numero_admin",
                it
            )
        }
        if (effectue == false) {
            Log.d("TruckerService", "Ajout d'un numéro admin dans la liste blanche impossible car déjà présent: $donnee")
            logcatControleur.ecrireDansFichierLog("Ajout d'un numéro admin dans la liste blanche impossible car déjà présent: $donnee")
            return false
        }
        Log.d("TruckerService", "Ajout d'un numéro admin dans la liste blanche : $donnee")
        logcatControleur.ecrireDansFichierLog("Ajout d'un numéro admin dans la liste blanche : $donnee")
        return true
    }
}