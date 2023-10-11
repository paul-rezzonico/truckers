package com.unilim.iut.truckers.model

data class Message(private val phoneNumber: PhoneNumber, private val message: String, private val date: String) {

    companion object {
        private var nextId = 1
    }

    private val id: Int = nextId++
    private var isSynchronized: Boolean = false
}