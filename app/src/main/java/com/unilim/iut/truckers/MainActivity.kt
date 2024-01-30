package com.unilim.iut.truckers

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.unilim.iut.truckers.controleur.SynchronisationControleur
import com.unilim.iut.truckers.utile.GestionnaireDePermissions
import com.unilim.iut.truckers.utile.InitialisateurKoin
import com.unilim.iut.truckers.utile.MiseEnFileDuGestionnaireDeTravaux
import org.koin.android.ext.android.inject

class MainActivity : Activity() {

    private val gestionnaireDePermissions by lazy { GestionnaireDePermissions(this) }
    private val initialisateurKoin by lazy { InitialisateurKoin(this) }
    private val miseEnFileDuGestionnaireDeTravaux by lazy { MiseEnFileDuGestionnaireDeTravaux(this) }
    private val synchronisationControleur: SynchronisationControleur by inject()

    override fun onCreate(instanceEtatSauvegardee: Bundle?) {
        super.onCreate(instanceEtatSauvegardee)

        initialisateurKoin.setupKoin()
        gestionnaireDePermissions.requestPermissions()
    }

    override fun onResume() {
        super.onResume()
        handlePermissionsAndEnqueueJob()
    }

    private fun handlePermissionsAndEnqueueJob() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            if (gestionnaireDePermissions.areAllPermissionsGranted()) {
                miseEnFileDuGestionnaireDeTravaux.mettreEnFile()
                synchronisationControleur.miseEnPlaceSynchronisation()
                finish()
            } else {
                gestionnaireDePermissions.requestSMSPermissions()
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && gestionnaireDePermissions.areAllPermissionsGranted()) {
            miseEnFileDuGestionnaireDeTravaux.mettreEnFile()
            synchronisationControleur.miseEnPlaceSynchronisation()
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        gestionnaireDePermissions.handlePermissionsResult(requestCode, grantResults) {
            if (it) {
                Log.d("Permissions", "SMS permission accordée")
                if (gestionnaireDePermissions.areAllPermissionsGranted()) {
                    miseEnFileDuGestionnaireDeTravaux.mettreEnFile()
                    synchronisationControleur.miseEnPlaceSynchronisation()
                    finish()
                }
            } else {
                Log.d("Permissions", "SMS permission refusée")
            }
        }
    }
}