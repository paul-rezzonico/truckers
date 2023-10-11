package com.unilim.iut.truckers.model

data class Message(private val phoneNumber: PhoneNumber, private val message: String, private val dateReception: String) {

    companion object {
        private var nextId = 1
    }

    private val id: Int = nextId++
    private var estSynchro: Boolean = false
}