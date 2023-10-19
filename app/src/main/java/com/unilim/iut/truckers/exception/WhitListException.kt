package com.unilim.iut.truckers.exception

class WhitListException : Exception() {
    override val message: String
        get() = "Erreur lors de la lecture/écriture du fichier JSON WhiteList : "
}