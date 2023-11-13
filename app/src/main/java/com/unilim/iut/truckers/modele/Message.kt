package com.unilim.iut.truckers.modele

data class Message(
    val numeroTelephone: NumeroTelephone,
    val message: String,
    val dateReception: String,
    val id: Int = nextId++,
    var estSynchro: Boolean = false
) {
    companion object {
        private var nextId = 1
    }
}