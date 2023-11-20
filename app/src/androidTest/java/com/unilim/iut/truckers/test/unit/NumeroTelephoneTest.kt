package com.unilim.iut.truckers.test.unit

import com.unilim.iut.truckers.modele.NumeroTelephone
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class NumeroTelephoneTest {

    @Test
    fun devraitLancerExceptionSiNumeroDeTelephoneNonValide() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            NumeroTelephone("002345678")
        }
    }

    @Test
    fun neDevraitPasLancerExceptionSiNumeroDeTelephoneValide() {
        val numeroDeTelephone = NumeroTelephone("0123456789")

        assertThat("0123456789", Matchers.equalTo(numeroDeTelephone.numeroTelephone))
    }

    @Test
    fun devraitLancerExceptionSiNumeroDeTelephoneTropLong() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            NumeroTelephone("01234567890")
        }
    }

    @Test
    fun devraitLancerExceptionSiNumeroDeTelephoneTropCourt() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            NumeroTelephone("012345678")
        }
    }
}
