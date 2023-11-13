package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log

class SupprimerNumeroAdminCommande(
    override var context: Context?,
    override var donnee: Any?
) : Commande() {

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            jsonControleur.supprimer(
                context, "ListeBlanche.json", "numero_admin",
                it
            )
        }
        if (effectue == false) {
            Log.d("TruckerService", "Suppression d'un numéro admin dans la liste blanche impossible: $donnee")
            return false
        }
        Log.d("TruckerService", "Suppression d'un numéro admin dans la liste blanche: $donnee")
        return true
    }
}