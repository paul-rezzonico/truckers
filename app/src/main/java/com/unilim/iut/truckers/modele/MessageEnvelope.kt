package com.unilim.iut.truckers.modele
data class MessageEnvelope(
    val numero: String,
    val messages: List<Message>
) {
    override fun toString(): String {
        return "{\"numero\":\"$numero\",\"messages\": $messages}"
    }
}
