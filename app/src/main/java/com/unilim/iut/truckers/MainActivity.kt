package com.unilim.iut.truckers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unilim.iut.truckers.command.AddAdminNumberCommand
import com.unilim.iut.truckers.command.AddKeyWordCommand
import com.unilim.iut.truckers.command.AddWhiteListNumberCommand
import com.unilim.iut.truckers.command.Command
import com.unilim.iut.truckers.command.CommandHistory
import com.unilim.iut.truckers.controller.CommandController
import com.unilim.iut.truckers.controller.DefaultController
import com.unilim.iut.truckers.controller.KeyWordController
import com.unilim.iut.truckers.controller.MessageController
import com.unilim.iut.truckers.controller.WhiteListController
import com.unilim.iut.truckers.service.SmsReceiverService
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    companion object {
        val history: CommandHistory = CommandHistory()
    }

    private val controlleurCommande = CommandController()
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
        applicationContext.getSharedPreferences("sms_service_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("is_receiver_registered", false)
            .apply()

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
            val listeNumeroAdmin = controlleurDefaut.chargementListeBlancheDefaut(this, "numero_admin")

            for (numero in listeBlanche) {
                controlleurCommande.executerCommande(AddWhiteListNumberCommand(this, numero))
            }
            for (numeroAdmin in listeNumeroAdmin) {
                controlleurCommande.executerCommande(AddAdminNumberCommand(this, numeroAdmin))
            }
            for (motcle in listeMotsCles) {
                controlleurCommande.executerCommande(AddKeyWordCommand(this, motcle))
            }

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
