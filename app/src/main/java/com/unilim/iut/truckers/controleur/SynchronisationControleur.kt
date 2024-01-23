package com.unilim.iut.truckers.controleur

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.unilim.iut.truckers.api.ApiManager
import com.unilim.iut.truckers.modele.MessageEnvelope
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SynchronisationControleur(contexte: Context) {

    private lateinit var executeur: ScheduledExecutorService
    private var intervalleMinutes: Long = 5

    @SuppressLint("HardwareIds")
    private val androidId = Settings.Secure.getString(contexte.contentResolver, Settings.Secure.ANDROID_ID)

    private val tache = Runnable {
        Log.d("TruckerService", "--------------------SYNC-----------------------")
        LogcatControleur().ecrireDansFichierLog("--------------------SYNC-----------------------")
        val messageControleur = MessageControleur()
        val listeMessageValide = messageControleur.avoirMessagesDansBonJsonMessage(contexte)
        val listeMessageInvalide = messageControleur.avoirMessagesDansMauvaisJsonMessage(contexte)
        val messageEnvelopeValide = MessageEnvelope(androidId, listeMessageValide)
        val messageEnvelopeInvalide = MessageEnvelope(androidId, listeMessageInvalide)
        ApiManager().envoyerMessages(messageEnvelopeValide, "sync_msg")
        ApiManager().envoyerMessages(messageEnvelopeInvalide, "sync_err")
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