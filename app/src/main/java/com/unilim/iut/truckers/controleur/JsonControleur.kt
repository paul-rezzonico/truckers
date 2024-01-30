package com.unilim.iut.truckers.controleur

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.unilim.iut.truckers.exception.LectureListeBlancheException
import com.unilim.iut.truckers.exception.EcritureListeBlancheException
import com.unilim.iut.truckers.facade.IFacadeDePersistence
import com.unilim.iut.truckers.modele.JsonData
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class JsonControleur : IFacadeDePersistence{

    private val jackson = ObjectMapper().registerModule(KotlinModule())
    private val logcatControleur = LogcatControleur()

    fun creationFichierJSON(jsonData: JsonData) {
        val fichier = File(jsonData.contexte?.filesDir, jsonData.cheminFichier)
        if (fichier.exists()) {
            Log.d("TruckerService", "Le fichier JSON existe déjà : ${jsonData.cheminFichier}")
            logcatControleur.ecrireDansFichierLog("Le fichier JSON existe déjà : ${jsonData.cheminFichier}")
            return
        }
        Log.d("TruckerService", "Création du fichier JSON : ${jsonData.cheminFichier}")
        logcatControleur.ecrireDansFichierLog("Création du fichier JSON : ${jsonData.cheminFichier}")

        val objetJson = JSONObject()
        jsonData.champs?.let { objetJson.put(it, JSONArray()) }

        try {
            val fluxSortie: FileOutputStream? =
                jsonData.contexte?.openFileOutput(jsonData.cheminFichier, Context.MODE_PRIVATE)

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
     * @param jsonData Ce paramètre est l'objet JsonData contenant le contexte de l'application et le chemin du fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun suppressionFichierJSON(jsonData: JsonData): Boolean {
        val fichier = File(jsonData.contexte?.filesDir, jsonData.cheminFichier)
        if (fichier.exists()) {
            val isDeleted = fichier.delete()
            if (isDeleted) {
                Log.d("TruckerService", "Le fichier JSON a été supprimé : ${jsonData.cheminFichier}")
                logcatControleur.ecrireDansFichierLog("Le fichier JSON a été supprimé : ${jsonData.cheminFichier}")
            } else {
                Log.e("TruckerService", "Échec de la suppression du fichier JSON : ${jsonData.cheminFichier}")
                logcatControleur.ecrireDansFichierLog("Échec de la suppression du fichier JSON : ${jsonData.cheminFichier}")
            }
            return isDeleted
        }
        return false
    }

    /**
     * Cette fonction permet d'ajouter un objet Message dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et de l'objet Message.
     *
     * @param jsonData Ce paramètre est l'objet JsonData contenant le contexte de l'application, le chemin du fichier JSON, le nom de l'objet JSON et l'objet Message.
     * @return Cette fonction ne retourne rien.
     */
    override fun sauvegarderDonneesDansJSON(jsonData: JsonData): Boolean {

        val donneesSerialisees = jackson.writeValueAsString(jsonData.donnees)
        Log.d("TruckerService", donneesSerialisees)

        val fichier = File(jsonData.contexte?.filesDir, jsonData.cheminFichier)
        if (!fichier.exists()) {
            creationFichierJSON(jsonData)
            return false
        }

        val fluxEntree: FileInputStream? = jsonData.contexte?.openFileInput(jsonData.cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val donneesJson : JSONArray? = if (json.toString() == "{}" || json?.has(jsonData.champs) == false) {
            JSONArray()
        } else {
            jsonData.champs?.let { json?.getJSONArray(it) }
        }

        if (donneesJson != null) {
            for (i in 0 until donneesJson.length()) {
                if (donneesJson.getString(i) == donneesSerialisees) {
                    return false
                }
            }
        }

        donneesJson?.put(donneesSerialisees)
        jsonData.champs?.let { json?.put(it, donneesJson) }

        return try {
            val fluxSortie: FileOutputStream? =
                jsonData.contexte?.openFileOutput(jsonData.cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(json?.toString(4)?.toByteArray())
            fluxSortie?.close()
            true
        } catch (e: EcritureListeBlancheException) {
            Log.d("TruckerService", e.message)
            logcatControleur.ecrireDansFichierLog(e.message)
            false
        }
    }

    /**
     * Cette fonction permet de supprimer un objet Message dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et de l'objet Message.
     *
     * @param jsonData Ce paramètre est l'objet JsonData contenant le contexte de l'application, le chemin du fichier JSON, le nom de l'objet JSON, l'objet Message et le nombre de message enregistré.
     * @return Cette fonction ne retourne rien.
     */
    override fun supprimerDonneesDansJSON(jsonData: JsonData): Boolean {
        val donneesSerialisees = jackson.writeValueAsString(jsonData.donnees)

        val fichier = File(jsonData.contexte?.filesDir, jsonData.cheminFichier)
        if (!fichier.exists()) {
            creationFichierJSON(jsonData)
            return false
        }

        val json = chargerDonneesDuJSON(jsonData)
        val liste: JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            jsonData.champs?.let { json.getJSONArray(it) }
        }

        if (jsonData.champs != "numero_admin" && liste != null) {
            jsonData.nombreMessageEnregistre?.let {
                supprimerDonneesTableauJSON(liste, donneesSerialisees,
                    it
                )
            }
            jsonData.champs?.let { json.put(it, liste) }
            ajouterDonneesJSONListeBlancheDansFichier(jsonData, json)
            return true
        }

        return false
    }


    /**
     * Cette fonction permet de charger le contenu d'un fichier JSON.
     *
     * @param jsonData Ce paramètre est l'objet JsonData contenant le contexte de l'application et le chemin du fichier JSON.
     * @return Cette fonction retourne le contenu du fichier JSON.
     */
    override fun chargerDonneesDuJSON(jsonData: JsonData): JSONObject {
        var objetJson = JSONObject()

        try {
            val fluxEntree: FileInputStream? = jsonData.contexte?.openFileInput(jsonData.cheminFichier)
            if (fluxEntree != null && File(jsonData.contexte.filesDir, jsonData.cheminFichier).exists()) {
                val jsonStr = fluxEntree.bufferedReader().use { it.readText() }
                objetJson = JSONObject(jsonStr)
            } else {
                Log.d("TruckerService", "Le fichier JSON n'existe pas : ${jsonData.cheminFichier}")
            }
        } catch (e: LectureListeBlancheException) {
            Log.d("TruckerService", e.message)
            logcatControleur.ecrireDansFichierLog(e.message)
        }

        return objetJson
    }

    private fun supprimerDonneesTableauJSON(liste: JSONArray, donneesSerialisees: String, nombreMessageEnregistre: Int) {
        if (nombreMessageEnregistre == -1) {
            supprimerDonneeUnique(liste, donneesSerialisees)
        } else {
            supprimerMessages(liste, nombreMessageEnregistre)
        }
    }

    private fun supprimerDonneeUnique(liste: JSONArray, donneesSerialisees: String) {
        for (i in 0 until liste.length()) {
            if (liste.getString(i) == donneesSerialisees) {
                liste.remove(i)
                break
            }
        }
    }

    private fun supprimerMessages(liste: JSONArray, nombreMessageEnregistre: Int) {
        for (i in 0 until nombreMessageEnregistre) {
            liste.remove(0)
        }
    }

    private fun ajouterDonneesJSONListeBlancheDansFichier(jsonData: JsonData, json: JSONObject?) {
        try {
            val fluxSortie: FileOutputStream? =
                jsonData.contexte?.openFileOutput(jsonData.cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(json?.toString(4)?.toByteArray())
            fluxSortie?.close()
        } catch (e: EcritureListeBlancheException) {
            Log.d("TruckerService", e.message)
            logcatControleur.ecrireDansFichierLog(e.message)
        }
    }
}
