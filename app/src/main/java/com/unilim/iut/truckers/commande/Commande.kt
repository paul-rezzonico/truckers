package com.unilim.iut.truckers.commande

import android.content.Context
import com.unilim.iut.truckers.controleur.JsonControleur

abstract class Commande() {

    abstract var context: Context?
    abstract var donnee: Any?

    companion object {
        val jsonControleur: JsonControleur = JsonControleur()
    }
    abstract fun executer(): Boolean
}