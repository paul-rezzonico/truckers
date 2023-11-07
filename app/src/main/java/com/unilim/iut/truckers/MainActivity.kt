package com.unilim.iut.truckers

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unilim.iut.truckers.controller.DefaultController
import com.unilim.iut.truckers.controller.KeyWordController
import com.unilim.iut.truckers.controller.MessageController
import com.unilim.iut.truckers.controller.WhiteListController
import com.unilim.iut.truckers.service.SmsReceiverService
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    private val controlleurListeBlanche = WhiteListController()
    private val controlleurMessage = MessageController()
    private val controlleurKeyWord = KeyWordController()
    private val controlleurDefaut = DefaultController()
    private val SMS_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), SMS_PERMISSION_CODE)
        }

        controlleurListeBlanche.creationListeBlanche(this)
        controlleurMessage.creationJsonMauvaisMessage(this)
        controlleurMessage.creationJsonBonMessage(this)
        controlleurKeyWord.creationJSONMotCle(this)

        if (controlleurListeBlanche.chargementListeBlanche(this, true).isNotEmpty() && controlleurKeyWord.chargementMotsCles(this).isNotEmpty()) {
            Log.d("TruckerService", "Liste blanche et mots clés déjà présents")
            val workManager = WorkManager.getInstance(this)
            val smsWorkerRequest = PeriodicWorkRequest.Builder(SmsReceiverService::class.java, 15, TimeUnit.MINUTES)
                .build()

            workManager.enqueue(smsWorkerRequest)
        } else if (controlleurDefaut.verificationDefaultJson(this)) {
            Log.d("TruckerService", "Liste blanche et mots clés par défaut")
            val listeMotsCles = controlleurDefaut.chargementMotsClesDefaut(this)
            val listeBlanche = controlleurDefaut.chargementListeBlancheDefaut(this, "liste_blanche")
            val numeroAdmin = controlleurDefaut.chargementListeBlancheDefaut(this, "numero_admin")

            controlleurListeBlanche.ajoutNumeroJSON(this, false, listeBlanche)
            controlleurListeBlanche.ajoutNumeroJSON(this, true, numeroAdmin)
            controlleurKeyWord.ajoutMotsCles(this, listeMotsCles)

            val workManager = WorkManager.getInstance(this)
            val smsWorkerRequest = PeriodicWorkRequest.Builder(SmsReceiverService::class.java, 15, TimeUnit.MINUTES)
                .build()

            workManager.enqueue(smsWorkerRequest)
        } else {
            Log.d("TruckerService", "Le fichier JSON par défaut n'existe pas")
        }

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
