package com.unilim.iut.truckers.model

data class PhoneNumber(val phoneNumber: String) {
    init {
        require(isValidFrenchPhoneNumberFormat(phoneNumber)) {
            "French phone number format is invalid"
        }
    }

    private fun isValidFrenchPhoneNumberFormat(phoneNumber: String): Boolean {
        // French phone number format is 0X XX XX XX XX where X is a digit between 0 and 9 (included)
        val regex = """0[1-9]\d{8}"""
        return phoneNumber.matches(regex.toRegex())
    }

    override fun toString(): String {
        return phoneNumber
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is PhoneNumber) {
            return false
        }
        return phoneNumber == other.phoneNumber
    }

    override fun hashCode(): Int {
        return phoneNumber.hashCode()
    }
}
