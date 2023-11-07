package com.unilim.iut.truckers.command

import android.content.Context
import com.unilim.iut.truckers.controller.JsonController

class DeleteKeyWordCommand(
    override var context: Context,
    override var donnee: Any?
) : Command() {

    override fun executer(): Boolean {
        donnee?.let {
            jsonController.supprimer(
                context, "MotsCles.json", "mots_cles",
                it
            )
        }
        return true
    }
}