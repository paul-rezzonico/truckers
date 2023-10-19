package com.unilim.iut.truckers.test.unit

import com.unilim.iut.truckers.model.PhoneNumber
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class PhoneNumberTest {

    @Test
    fun devraitLancerExceptionSiNumeroDeTelephoneNonValide() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PhoneNumber("002345678")
        }
    }

    @Test
    fun neDevraitPasLancerExceptionSiNumeroDeTelephoneValide() {
        val numeroDeTelephone = PhoneNumber("0123456789")

        assertThat("0123456789", Matchers.equalTo(numeroDeTelephone.phoneNumber))
    }

    @Test
    fun devraitLancerExceptionSiNumeroDeTelephoneTropLong() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PhoneNumber("01234567890")
        }
    }

    @Test
    fun devraitLancerExceptionSiNumeroDeTelephoneTropCourt() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PhoneNumber("012345678")
        }
    }
}
