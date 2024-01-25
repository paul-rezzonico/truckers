package com.unilim.iut.truckers.modele
data class MessageEnvelope(
    val idTelephone: String,
    val messages: List<Message>
) {
    override fun toString(): String {
        return "{\"idTelephone\":\"$idTelephone\",\"messages\": $messages}"
    }
}
