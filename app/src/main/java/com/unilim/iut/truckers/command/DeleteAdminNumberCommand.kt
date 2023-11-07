package com.unilim.iut.truckers.command

import android.content.Context
import com.unilim.iut.truckers.controller.JsonController

class DeleteAdminNumberCommand(
    override var context: Context,
    override var donnee: Any?
) : Command() {

    override fun executer(): Boolean {
        donnee?.let {
            jsonController.supprimer(
                context, "ListeBlanche.json", "numero_admin",
                it
            )
        }
        return true
    }
}