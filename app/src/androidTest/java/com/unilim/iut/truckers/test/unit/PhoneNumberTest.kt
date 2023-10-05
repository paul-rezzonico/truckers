package com.unilim.iut.truckers.test.unit

import com.unilim.iut.truckers.model.PhoneNumber
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class PhoneNumberTest {

    @Test
    fun shouldThrowExceptionWhenPhoneNumberIsInvalid() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PhoneNumber("002345678")
        }
    }

    @Test
    fun shouldNotThrowExceptionWhenPhoneNumberIsValid() {
        val phoneNumber = PhoneNumber("0123456789")

        assertThat("0123456789", Matchers.equalTo(phoneNumber.toString()))
    }

    @Test
    fun shouldThrowExceptionWhenPhoneNumberIsTooLong() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PhoneNumber("01234567890")
        }
    }

    @Test
    fun shouldThrowExceptionWhenPhoneNumberIsTooShort() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PhoneNumber("012345678")
        }
    }
}