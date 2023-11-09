package com.unilim.iut.truckers.controller

import com.unilim.iut.truckers.MainActivity
import com.unilim.iut.truckers.command.Command

class CommandController {

    /**
     * Execute une commande et l'ajoute à l'historique
     * @param commande la commande à executer
     */
    fun executerCommande(commande: Command) {
        commande.executer()
        MainActivity.history.ajouter(commande)
    }
}