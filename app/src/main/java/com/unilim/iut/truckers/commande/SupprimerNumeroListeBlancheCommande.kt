package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log

class SupprimerNumeroListeBlancheCommande(
    override var context: Context?,
    override var donnee: Any?
) : Commande() {

    override fun executer(): Boolean {
        donnee?.let {
            jsonControleur.supprimer(
                context, "ListeBlanche.json", "liste_blanche",
                it
            )
        }
        Log.d("TruckerService", "Suppression d'un numéro dans la liste blanche: $donnee")
        return true
    }
}