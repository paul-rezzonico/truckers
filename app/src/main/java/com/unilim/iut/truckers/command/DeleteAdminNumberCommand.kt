package com.unilim.iut.truckers.command

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.controller.JsonController

class DeleteAdminNumberCommand(
    override var context: Context?,
    override var donnee: Any?
) : Command() {

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            jsonController.supprimer(
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