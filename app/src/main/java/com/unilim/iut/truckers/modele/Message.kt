package com.unilim.iut.truckers.modele

data class Message(
    val id: Int = obtenirNouvelId(),
    val numeroTelephone: String,
    val message: String,
    val dateReception: String,
    var estSynchro: Boolean = false
) {
    companion object {
        private var compteurIds = 0

        fun obtenirNouvelId(): Int {
            return compteurIds++
        }
    }
}