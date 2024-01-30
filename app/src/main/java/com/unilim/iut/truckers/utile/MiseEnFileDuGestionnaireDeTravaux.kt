package com.unilim.iut.truckers.utile

import android.content.Context
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unilim.iut.truckers.commande.AjoutMotCleCommande
import com.unilim.iut.truckers.commande.AjoutNumeroAdminCommande
import com.unilim.iut.truckers.commande.AjoutNumeroListeBlancheCommande
import com.unilim.iut.truckers.controleur.CommandeControleur
import com.unilim.iut.truckers.controleur.DefautControleur
import com.unilim.iut.truckers.controleur.ListeBlancheControleur
import com.unilim.iut.truckers.controleur.MessageControleur
import com.unilim.iut.truckers.controleur.MotCleControleur
import com.unilim.iut.truckers.service.ServiceDuReceveurDeSMS
import java.util.concurrent.TimeUnit

class MiseEnFileDuGestionnaireDeTravaux(private val contexte: Context) {

    private val controleurListeBlanche = ListeBlancheControleur()
    private val controleurMessage = MessageControleur()
    private val controleurMotCles = MotCleControleur()
    private val controleurDefaut = DefautControleur()
    private val controleurCommande = CommandeControleur()

    fun mettreEnFile() {
        Log.d("TruckerService", "-----------------Démarrage du service----------------")
        controleurListeBlanche.creationListeBlanche(contexte)
        controleurMessage.creationJsonMauvaisMessage(contexte)
        controleurMessage.creationJsonBonMessage(contexte)
        controleurMotCles.creationJSONMotCle(contexte)
        contexte.applicationContext.getSharedPreferences("sms_service_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("is_receiver_registered", false)
            .apply()

        if (controleurListeBlanche.chargementListeBlanche(contexte, true).isNotEmpty() && controleurMotCles.chargementMotsCles(contexte).isNotEmpty()) {
            Log.d("TruckerService", "Liste blanche et mots clés déjà présents")
            val managerDeTravail = WorkManager.getInstance(contexte)
            val smsWorkerRequest = PeriodicWorkRequest.Builder(ServiceDuReceveurDeSMS::class.java, 15, TimeUnit.MINUTES)
                .build()

            managerDeTravail.enqueue(smsWorkerRequest)
        } else if (controleurDefaut.verificationDefaultJson()) {
            Log.d("TruckerService", "Liste blanche et mots clés par défaut")
            val listeMotsCles = controleurDefaut.chargementMotsClesDefaut(contexte)
            val listeBlanche = controleurDefaut.chargementListeBlancheDefaut(contexte, "liste_blanche")
            val listeNumeroAdmin = controleurDefaut.chargementListeBlancheDefaut(contexte, "numero_admin")
            Log.d("TruckerService", "------------Liste blanche-------------")
            for (numero in listeBlanche) {
                controleurCommande.executerCommande(AjoutNumeroListeBlancheCommande(contexte, numero))
            }
            Log.d("TruckerService", "-------------Numéro admin-------------")
            for (numeroAdmin in listeNumeroAdmin) {
                controleurCommande.executerCommande(AjoutNumeroAdminCommande(contexte, numeroAdmin))
            }
            Log.d("TruckerService", "--------------Mots clés---------------")
            for (motcle in listeMotsCles) {
                controleurCommande.executerCommande(AjoutMotCleCommande(contexte, motcle))
            }
            val managerDeTravail = WorkManager.getInstance(contexte)
            val requeteDuTravailleurDeSms = PeriodicWorkRequest.Builder(ServiceDuReceveurDeSMS::class.java, 15, TimeUnit.MINUTES)
                .build()
            managerDeTravail.enqueue(requeteDuTravailleurDeSms)
        } else {
            Log.d("TruckerService", "Le fichier JSON par défaut n'existe pas")
        }
    }
}