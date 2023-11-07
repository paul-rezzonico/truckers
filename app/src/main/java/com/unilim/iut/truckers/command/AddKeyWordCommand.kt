package com.unilim.iut.truckers.command

import android.content.Context

class AddKeyWordCommand(
    override var context: Context,
    override var donnee: Any?
) : Command() {

    override fun executer(): Boolean {
        donnee?.let {
            jsonController.sauvegarder(context, "MotsCles.json", "mots_cles",
                it
            )
        }
        return true
    }
}