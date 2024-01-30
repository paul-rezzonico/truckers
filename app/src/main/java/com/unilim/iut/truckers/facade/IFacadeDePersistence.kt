package com.unilim.iut.truckers.facade

import com.unilim.iut.truckers.modele.JsonData

interface IFacadeDePersistence {

    fun sauvegarderDonneesDansJSON(jsonData: JsonData): Boolean
    fun chargerDonneesDuJSON(jsonData: JsonData) : Any
    fun supprimerDonneesDansJSON(jsonData: JsonData): Boolean
}