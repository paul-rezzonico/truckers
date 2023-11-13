package com.unilim.iut.truckers.commande

import android.content.Context
import com.unilim.iut.truckers.controleur.JsonControleur

abstract class Commande() {

    abstract var context: Context?
    abstract var donnee: Any?

    companion object {
        val jsonControleur: JsonControleur = JsonControleur()
    }

    constructor(context: Context, donnee: Any?): this() {
        this.context = context
        this.donnee = donnee
    }

    abstract fun executer(): Boolean
}