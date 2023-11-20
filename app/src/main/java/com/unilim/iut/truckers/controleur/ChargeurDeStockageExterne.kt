package com.unilim.iut.truckers.controleur

import android.content.Context
import android.os.Environment
import android.util.Log
import com.unilim.iut.truckers.facade.IFacadeDePersistence
import org.json.JSONObject
import java.io.File

class ChargeurDeStockageExterne : IFacadeDePersistence {
    private val logcatControleur = LogcatControleur()

    override fun sauvegarder(
        contexte: Context?,
        cheminFichier: String,
        champs: String,
        donnees: Any
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun charger(context: Context?, cheminFichier: String): JSONObject {
        var objetJson = JSONObject()

        try {
            // Path to the Downloads directory
            val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsPath, cheminFichier)

            if (file.exists()) {
                val jsonStr = file.readText(Charsets.UTF_8)
                objetJson = JSONObject(jsonStr)
            } else {
                Log.d("TruckerService", "File not found in Downloads folder")
            }
        } catch (e: Exception) { // Catch a more general exception
            Log.d("TruckerService", "Error reading JSON file: ${e.message}")
            logcatControleur.ecrireDansFichierLog("Error reading JSON file: ${e.message}")
        }

        return objetJson
    }

    override fun supprimer(
        contexte: Context?,
        cheminFichier: String,
        champs: String,
        donnees: Any
    ): Boolean {
        TODO("Not yet implemented")
    }
}
