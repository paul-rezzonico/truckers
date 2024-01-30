package com.unilim.iut.truckers.controleur

import android.os.Environment
import android.util.Log
import com.unilim.iut.truckers.facade.IFacadeDePersistence
import com.unilim.iut.truckers.modele.JsonData
import org.json.JSONObject
import java.io.File

class ChargeurDeStockageExterne : IFacadeDePersistence {
    private val logcatControleur = LogcatControleur()

    override fun sauvegarderDonneesDansJSON(
        jsonData: JsonData
    ): Boolean {
        TODO("Not needed here")
    }

    override fun chargerDonneesDuJSON(jsonData: JsonData): JSONObject {
        var objetJson = JSONObject()

        try {
            val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsPath, jsonData.cheminFichier)

            if (file.exists()) {
                    val jsonStr = file.readText(Charsets.UTF_8)
                    objetJson = JSONObject(jsonStr)
                } else {
                    Log.d("TruckerService", "Fichier JSOn non trouvé dans le répertoire Downloads")
                }
        } catch (e: Exception) {
            Log.d("TruckerService", "Erreur lors de la lecture du fichier JSON, ${e.message}")
            logcatControleur.ecrireDansFichierLog("Erreur lors de la lecture du fichier JSON, ${e.message}")
        }

        return objetJson
    }

    override fun supprimerDonneesDansJSON(
        jsonData: JsonData
    ): Boolean {
        TODO("Not yet implemented")
    }
}
