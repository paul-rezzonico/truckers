package com.unilim.iut.truckers.modele

import android.content.Context

data class JsonData(
    val contexte: Context?,
    val cheminFichier: String,
    val champs: String?,
    val donnees: Any?,
    val nombreMessageEnregistre: Int?
)
