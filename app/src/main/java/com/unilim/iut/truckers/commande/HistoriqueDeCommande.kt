package com.unilim.iut.truckers.commande

import java.util.Stack

class HistoriqueDeCommande {
    private val historique: Stack<Commande> = Stack()

    fun ajouter(commande: Commande) {
        historique.push(commande)
    }

    fun annuler(): Boolean {
        if (historique.isEmpty()) {
            return false
        }
        historique.pop()
        return true
    }

    fun vider() {
        historique.clear()
    }

    fun EstVide(): Boolean {
        return historique.isEmpty()
    }

    fun historique(): Stack<Commande> {
        return historique
    }
}