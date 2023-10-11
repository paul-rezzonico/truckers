package com.unilim.iut.truckers.model

data class PhoneNumber(val phoneNumber: String) {
    init {
        require(isValidFrenchPhoneNumberFormat(phoneNumber)) {
            "Le numéro de téléphone doit être au format international français"
        }
    }

    private fun isValidFrenchPhoneNumberFormat(phoneNumber: String): Boolean {
        // Numéro de téléphone français au format international :
        //  0X XX XX XX XX (10 chiffres) avec X compris entre 1 et 9
        val regex = """0[1-9]\d{8}"""
        return phoneNumber.matches(regex.toRegex())
    }
}
