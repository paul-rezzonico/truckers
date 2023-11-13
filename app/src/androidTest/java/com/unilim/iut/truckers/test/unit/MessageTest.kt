package com.unilim.iut.truckers.test.unit

import com.unilim.iut.truckers.modele.Message
import com.unilim.iut.truckers.modele.NumeroTelephone
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MessageTest {
    @Test
    fun devraitCreerUnMessageAvecIdDeux() {
        val message = Message(NumeroTelephone("0987654321"),
        "Premier Message",
        "01/01/2020 00:00:00")

        val message2 = Message(NumeroTelephone("0123456789"),
    "Deuxième Message",
    "01/01/2020 00:00:00")

        assertThat(1, equalTo(message.id))
        assertThat(2, equalTo(message2.id))
    }
}