package com.unilim.iut.truckers.facade

import android.content.Context
import com.unilim.iut.truckers.model.Message

interface IFacadeDePersistence {

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de String mot-clés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun sauvegarder(contexte: Context?, cheminFichier: String, champs: String, donnees: Any)
    fun charger(context: Context?, cheminFichier: String) : Any
    fun supprimer(contexte: Context?, cheminFichier: String, champs: String, donnees: Any)
}