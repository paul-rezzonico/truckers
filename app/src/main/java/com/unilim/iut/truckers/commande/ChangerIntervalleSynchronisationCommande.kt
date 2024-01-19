package com.unilim.iut.truckers.commande

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.MainActivity
import com.unilim.iut.truckers.controleur.LogcatControleur

class ChangerIntervalleSynchronisationCommande (
    override var context: Context?,
    override var donnee: Any?
) : Commande() {

    private val logcatControleur = LogcatControleur()

    override fun executer(): Boolean {
        val effectue = donnee?.let {
            MainActivity.controleurSynchronisation.changerIntervalle(it.toString().toLong())
            MainActivity.controleurSynchronisation.arreterSynchronisation()
            MainActivity.controleurSynchronisation.miseEnPlaceSynchronisation()
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