package com.unilim.iut.truckers.controleur

import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SynchronisationControleur {

    private lateinit var executeur: ScheduledExecutorService
    private var intervalleMinutes: Long = 5
    private val tache = Runnable {
        Log.d("TruckerService", "Synchronisation des messages")
    }

    fun changerIntervalle(intervalle: Long): Boolean {
        this.intervalleMinutes = intervalle
        return true
    }

    /**
     * Cette méthode permet de lancer la synchronisation à intervalle régulier
     * return void
     */
    fun miseEnPlaceSynchronisation() {
        executeur = Executors.newScheduledThreadPool(1)
        executeur.scheduleAtFixedRate(tache, 0, intervalleMinutes, TimeUnit.MINUTES)
    }

    /**
     * Cette méthode permet d'arrêter la synchronisation
     * return void
     */
    fun arreterSynchronisation() {
        executeur.shutdown()
    }
}