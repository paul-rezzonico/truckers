package com.unilim.iut.truckers.exception

class ReadWhiteListException : Exception() {
    override val message: String
        get() = "Erreur lors de la lecture du fichier JSON WhiteList : "
}