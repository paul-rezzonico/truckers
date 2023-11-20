package com.unilim.iut.truckers.controleur

import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class LogcatControleur() {

    private val nomFichierLog = "logcat.txt"

    /**
     * Cette fonction permet d'écrire un message dans le fichier logcat.txt.
     *
     * @param message Ce paramètre est le message à écrire dans le fichier logcat.txt.
     * @return Cette fonction ne retourne rien.
     */
    fun ecrireDansFichierLog(message: String) {
        val dossierTelechargement = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val fichierLog = File(dossierTelechargement, nomFichierLog)
        if (!fichierLog.exists()) {
            fichierLog.createNewFile()
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        val timestamp = dateFormat.format(date)

        val messageFormate = "$timestamp: $message\n"

        try {
            val fluxDeSortieDuFichier = FileOutputStream(fichierLog, true)
            fluxDeSortieDuFichier.write(messageFormate.toByteArray())
            fluxDeSortieDuFichier.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Cette fonction permet de supprimer le fichier logcat.txt.
     */
    fun supprimerFichierLog() {
        val dossierTelechargement = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fichierLog = File(dossierTelechargement, nomFichierLog)
        if (fichierLog.exists()) {
            fichierLog.delete()
        }
    }
}
