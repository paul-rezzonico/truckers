package com.unilim.iut.truckers.facade

import android.content.Context

interface IFacadeDePersistence {

    fun sauvegarderDonneesDansJSON(contexte: Context?, cheminFichier: String, champs: String, donnees: Any): Boolean
    fun chargerDonneesDuJSON(context: Context?, cheminFichier: String) : Any
    fun supprimerDonneesDansJSON(contexte: Context?, cheminFichier: String, champs: String, donnees: Any): Boolean
}