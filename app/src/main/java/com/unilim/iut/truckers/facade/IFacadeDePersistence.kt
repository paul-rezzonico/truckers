package com.unilim.iut.truckers.facade

import android.content.Context
import com.unilim.iut.truckers.model.Message

interface IFacadeDePersistence {

    fun sauvegarder(contexte: Context?, cheminFichier: String, champs: String, donnees: Any)
    fun charger(context: Context?, cheminFichier: String) : Any
    fun supprimer(contexte: Context?, cheminFichier: String, champs: String, donnees: Any)
}