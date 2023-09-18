package com.unilim.iut.truckers

class Message(val sourceNumber: String, val body: String) {

    fun Show(): String{
        return "SMS reçu de $sourceNumber : $body"
    }

}