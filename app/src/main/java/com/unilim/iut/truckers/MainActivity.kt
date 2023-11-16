package com.unilim.iut.truckers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unilim.iut.truckers.commande.AjoutNumeroAdminCommande
import com.unilim.iut.truckers.commande.AjoutMotCleCommande
import com.unilim.iut.truckers.commande.AjoutNumeroListeBlancheCommande
import com.unilim.iut.truckers.commande.HistoriqueDeCommande
import com.unilim.iut.truckers.controleur.CommandeControleur
import com.unilim.iut.truckers.controleur.DefautControleur
import com.unilim.iut.truckers.controleur.MotCleControleur
import com.unilim.iut.truckers.controleur.MessageControleur
import com.unilim.iut.truckers.controleur.ListeBlancheControleur
import com.unilim.iut.truckers.controleur.LogcatControleur
import com.unilim.iut.truckers.service.ServiceDuReceveurDeSMS
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    companion object {
        val history: HistoriqueDeCommande = HistoriqueDeCommande()
    }

    private val controleurLogcat = LogcatControleur()
    private val controlleurCommande = CommandeControleur()
    private val controlleurListeBlanche = ListeBlancheControleur()
    private val controlleurMessage = MessageControleur()
    private val controlleurKeyWord = MotCleControleur()
    private val controlleurDefaut = DefautControleur()
    private val SMS_PERMISSION_CODE = 123

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), SMS_PERMISSION_CODE)
        }else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), SMS_PERMISSION_CODE)
        }

        controleurLogcat.supprimerFichierLog()
        controlleurListeBlanche.creationListeBlanche(this)
        controlleurMessage.creationJsonMauvaisMessage(this)
        controlleurMessage.creationJsonBonMessage(this)
        controlleurKeyWord.creationJSONMotCle(this)
        applicationContext.getSharedPreferences("sms_service_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("is_receiver_registered", false)
            .apply()

        if (controlleurListeBlanche.chargementListeBlanche(this, true).isNotEmpty() && controlleurKeyWord.chargementMotsCles(this).isNotEmpty()) {
            Log.d("TruckerService", "Liste blanche et mots clés déjà présents")
            Toast.makeText(this, "Liste blanche et mots clés déjà présents", Toast.LENGTH_LONG).show()
            val workManager = WorkManager.getInstance(this)
            val smsWorkerRequest = PeriodicWorkRequest.Builder(ServiceDuReceveurDeSMS::class.java, 15, TimeUnit.MINUTES)
                .build()

            workManager.enqueue(smsWorkerRequest)
        } else if (controlleurDefaut.verificationDefaultJson(this)) {
            Log.d("TruckerService", "Liste blanche et mots clés par défaut")
            Toast.makeText(this, "Liste blanche et mots clés par défaut", Toast.LENGTH_LONG).show()
            val listeMotsCles = controlleurDefaut.chargementMotsClesDefaut(this)
            val listeBlanche = controlleurDefaut.chargementListeBlancheDefaut(this, "liste_blanche")
            val listeNumeroAdmin = controlleurDefaut.chargementListeBlancheDefaut(this, "numero_admin")

            for (numero in listeBlanche) {
                controlleurCommande.executerCommande(AjoutNumeroListeBlancheCommande(this, numero))
            }
            for (numeroAdmin in listeNumeroAdmin) {
                controlleurCommande.executerCommande(AjoutNumeroAdminCommande(this, numeroAdmin))
            }
            for (motcle in listeMotsCles) {
                controlleurCommande.executerCommande(AjoutMotCleCommande(this, motcle))
            }

            val workManager = WorkManager.getInstance(this)
            val smsWorkerRequest = PeriodicWorkRequest.Builder(ServiceDuReceveurDeSMS::class.java, 15, TimeUnit.MINUTES)
                .build()

            workManager.enqueue(smsWorkerRequest)
        } else {
            Log.d("TruckerService", "Le fichier JSON par défaut n'existe pas")
            Log.d("TruckerService", "Veuillez télécharger le fichier defaut.json sur le Drive")
            controleurLogcat.ecrireDansFichierLog("Le fichier JSON par défaut n'existe pas")
            controleurLogcat.ecrireDansFichierLog("Veuillez télécharger le fichier defaut.json sur le Drive")
        }

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
