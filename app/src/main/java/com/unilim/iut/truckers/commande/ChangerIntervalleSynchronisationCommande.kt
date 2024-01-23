package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.controleur.LogcatControleur
import com.unilim.iut.truckers.controleur.SynchronisationControleur
import org.koin.java.KoinJavaComponent.inject

class ChangerIntervalleSynchronisationCommande (
    override var context: Context?,
    override var donnee: Any?
) : Commande() {

    private val logcatControleur = LogcatControleur()
    private val synchronisationControleur: SynchronisationControleur by inject(SynchronisationControleur::class.java)

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            synchronisationControleur.changerIntervalle(it.toString().toLong())
            synchronisationControleur.arreterSynchronisation()
            synchronisationControleur.miseEnPlaceSynchronisation()
            true
        } ?: false

        if (!effectue) {
            Log.d("TruckerService", "Changement de l'intervalle impossible: $donnee minutes")
            logcatControleur.ecrireDansFichierLog("Changement de l'intervalle impossible: $donnee minutes")
            return false
        }

        Log.d("TruckerService", "Changement de l'intervalle effectué: $donnee minutes")
        logcatControleur.ecrireDansFichierLog("Changement de l'intervalle effectué: $donnee minutes")
        return true
    }
}