package com.unilim.iut.truckers.controleur

import com.unilim.iut.truckers.commande.Commande

class CommandeControleur {

    /**
     * Execute une commande et l'ajoute à l'historique
     * @param commande la commande à exécuter
     */
    fun executerCommande(commande: Commande) {
        commande.executer()
    }
}