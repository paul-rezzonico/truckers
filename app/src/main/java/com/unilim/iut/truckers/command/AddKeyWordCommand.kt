package com.unilim.iut.truckers.command

import android.content.Context
import android.util.Log

class AddKeyWordCommand(
    override var context: Context?,
    override var donnee: Any?
) : Command() {

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            jsonController.sauvegarder(context, "MotsCles.json", "mots_cles",
                it
            )
        }
        if (effectue == false) {
            Log.d("TruckerService", "Ajout d'un mot clé impossible car déjà présent: $donnee")
            return false
        }
        Log.d("TruckerService", "Ajout d'un mot clé: $donnee")
        return true
    }
}