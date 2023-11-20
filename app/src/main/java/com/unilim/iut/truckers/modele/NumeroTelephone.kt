package com.unilim.iut.truckers.modele

data class NumeroTelephone(val numeroTelephone: String) {
    init {
        require(estUnNumeroFrancaisValide(numeroTelephone)) {
            "Le numéro de téléphone doit être au format international français"
        }
    }

    private fun estUnNumeroFrancaisValide(numeroTelephone: String): Boolean {
        // Numéro de téléphone français au format international :
        //  0X XX XX XX XX (10 chiffres) avec X compris entre 1 et 9
        val regex = """0[1-9]\d{8}"""
        return numeroTelephone.matches(regex.toRegex())
    }
}
