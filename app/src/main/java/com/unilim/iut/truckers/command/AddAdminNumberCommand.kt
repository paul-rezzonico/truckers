package com.unilim.iut.truckers.command

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.controller.JsonController

class AddAdminNumberCommand(
    override var context: Context?,
    override var donnee: Any?
): Command() {

    override fun executer(): Boolean {
        donnee?.let {
            jsonController.sauvegarder(context, "ListeBlanche.json", "numero_admin",
                it
            )
        }
        Log.d("TruckerService", "Ajout d'un numéro admin dans la liste blanche : $donnee")
        return true
    }
}