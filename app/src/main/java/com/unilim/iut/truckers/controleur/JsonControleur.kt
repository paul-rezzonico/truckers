package com.unilim.iut.truckers.controleur

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.unilim.iut.truckers.exception.LectureListeBlancheException
import com.unilim.iut.truckers.exception.EcritureListeBlancheException
import com.unilim.iut.truckers.facade.IFacadeDePersistence
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import android.os.Environment

class JsonControleur : IFacadeDePersistence{

    private val jackson = ObjectMapper().registerModule(KotlinModule())
    private val logcatControleur = LogcatControleur()

    fun creationJSON(contexte: Context?, cheminFichier: String, champs: String) {
        val fichier = File(contexte?.filesDir, cheminFichier)
        if (fichier.exists()) {
            Log.d("TruckerService", "Le fichier JSON existe déjà : $cheminFichier")
            logcatControleur.ecrireDansFichierLog("Le fichier JSON existe déjà : $cheminFichier")
            return
        }
        Log.d("TruckerService", "Création du fichier JSON : $cheminFichier")
        logcatControleur.ecrireDansFichierLog("Création du fichier JSON : $cheminFichier")

        val objetJson = JSONObject()
        objetJson.put(champs, null)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

        } catch (e: EcritureListeBlancheException) {
            Log.d("TruckerService", e.message)
            logcatControleur.ecrireDansFichierLog(e.message)
        }
    }

    /**
     * Cette fonction permet de supprimer un fichier JSON en fonction du chemin du fichier.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun supressionJSON(contexte: Context?, cheminFichier: String) {
        val fichier = File(contexte?.filesDir, cheminFichier)
        if (fichier.exists()) {
            fichier.delete()
            Log.d("TruckerService", "Le fichier JSON a été supprimé : $cheminFichier")
            logcatControleur.ecrireDansFichierLog("Le fichier JSON a été supprimé : $cheminFichier")
        }
    }

    /**
     * Cette fonction permet d'ajouter un objet Message dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et de l'objet Message.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param donnees Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param champs Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    override fun sauvegarder(contexte: Context?, cheminFichier: String, champs: String, donnees: Any): Boolean {

        val donneesSerialisees = jackson.writeValueAsString(donnees)

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            creationJSON(contexte, cheminFichier, champs)
            return false
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val donneesJson : JSONArray? = if (json.toString() == "{}" || json?.has(champs) == false) {
            JSONArray()
        } else {
            json?.getJSONArray(champs)
        }

        if (donneesJson != null) {
            for (i in 0 until donneesJson.length()) {
                if (donneesJson.getString(i) == donneesSerialisees) {
                    return false
                }
            }
        }

        donneesJson?.put(donneesSerialisees)
        json?.put(champs, donneesJson)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(json?.toString(4)?.toByteArray())
            fluxSortie?.close()
            return true
        } catch (e: EcritureListeBlancheException) {
            Log.d("TruckerService", e.message)
            logcatControleur.ecrireDansFichierLog(e.message)
            return false
        }
    }

    /**
     * Cette fonction permet de supprimer un objet Message dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et de l'objet Message.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param donnees Ce paramètre est l'objet Message à supprimer dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param champs Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    override fun supprimer(contexte: Context?, cheminFichier: String, champs: String, donnees: Any): Boolean {
        val donneesSerialisees = jackson.writeValueAsString(donnees)

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            creationJSON(contexte, cheminFichier, champs)
            return false
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val liste : JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            json?.getJSONArray(champs)
        }

        if (champs != "numero_admin") {
            for (i in 0 until liste!!.length()) {
                Log.d("TruckerService", liste.getString(i))
                logcatControleur.ecrireDansFichierLog(liste.getString(i))
                if (liste.getString(i) == donneesSerialisees) {
                    liste.remove(i)
                }
            }
        } else if (liste?.length()!! > 1) {
            for (i in 0 until liste.length()) {
                Log.d("TruckerService", liste.getString(i))
                logcatControleur.ecrireDansFichierLog(liste.getString(i))
                if (liste.getString(i) == donneesSerialisees) {
                    liste.remove(i)
                }
            }
        } else {
            return false
        }

        json?.put(champs, liste)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(json?.toString(4)?.toByteArray())
            fluxSortie?.close()
            return true
        } catch (e: EcritureListeBlancheException) {
            Log.d("TruckerService", e.message)
            logcatControleur.ecrireDansFichierLog(e.message)
            return false
        }
    }

    /**
     * Cette fonction permet de charger le contenu d'un fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @return Cette fonction retourne le contenu du fichier JSON.
     */
    override fun charger(context: Context?, cheminFichier: String): JSONObject {
        var objetJson = JSONObject()

        try {
            val fluxEntree: FileInputStream? = context?.openFileInput(cheminFichier)
            if (fluxEntree != null) {
                val jsonStr = fluxEntree.bufferedReader().use { it.readText() }
                objetJson = JSONObject(jsonStr)
            }
        } catch (e: LectureListeBlancheException) {
            Log.d("TruckerService", e.message)
            logcatControleur.ecrireDansFichierLog(e.message)
        }

        return objetJson
    }
}
