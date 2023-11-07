package com.unilim.iut.truckers.command

import java.util.Stack

class CommandHistory {
    private val historique: Stack<Command> = Stack()

    fun ajouter(command: Command) {
        historique.push(command)
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
}