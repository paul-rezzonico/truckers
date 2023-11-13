package com.unilim.iut.truckers.facade

import android.content.Context

interface IFacadeDePersistence {

    fun sauvegarder(contexte: Context?, cheminFichier: String, champs: String, donnees: Any): Boolean
    fun charger(context: Context?, cheminFichier: String) : Any
    fun supprimer(contexte: Context?, cheminFichier: String, champs: String, donnees: Any): Boolean
}