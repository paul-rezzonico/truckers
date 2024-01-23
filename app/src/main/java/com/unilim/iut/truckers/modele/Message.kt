package com.unilim.iut.truckers.modele

import java.util.UUID

data class Message(
    val id: String? = UUID.randomUUID().toString(),
    val envoyeur: String,
    val message: String,
    val dateReception: String,
) {
    constructor(envoyeur: String, message: String, dateReception: String) : this(
        id = UUID.randomUUID().toString(),
        envoyeur = envoyeur,
        message = message,
        dateReception = dateReception
    )

    override fun toString(): String {
        return "{\"id\":\"$id\",\"envoyeur\":\"$envoyeur\",\"message\":\"$message\",\"dateReception\":\"$dateReception\"}"
    }
}