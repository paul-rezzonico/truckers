package com.unilim.iut.truckers.model

data class Message(
    val phoneNumber: PhoneNumber,
    val message: String,
    val dateReception: String,
    val id: Int = nextId++,
    var estSynchro: Boolean = false
) {
    companion object {
        private var nextId = 1
    }
}