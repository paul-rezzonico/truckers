package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log

class SupprimerMotCleCommande(
    override var context: Context?,
    override var donnee: Any?
) : Commande() {

    override fun executer(): Boolean {
        donnee?.let {
            jsonControleur.supprimer(
                context, "MotsCles.json", "mots_cles",
                it
            )
        }
        Log.d("TruckerService", "Suppression d'un mot clé: $donnee")
        return true
    }
}