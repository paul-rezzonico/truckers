package com.unilim.iut.truckers.controller

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.unilim.iut.truckers.exception.ReadWhiteListException
import com.unilim.iut.truckers.exception.WriteWhiteListException
import com.unilim.iut.truckers.model.Message
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class JsonController {

    /**
     * Cette fonction permet de créer un fichier JSON en fonction du chemin du fichier et de l'objetJson.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param nomObjetJson Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJSON(contexte: Context?, cheminFichier: String, nomObjetJson: String) {
        val fichier = File(contexte?.filesDir, cheminFichier)
        if (fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $cheminFichier")
            return
        }

        val objetJson = JSONObject()
        objetJson.put(nomObjetJson, null)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Fichier JSON sauvegardé avec succès : $cheminFichier")
        } catch (e: WriteWhiteListException) {
            Log.d("SMSReceiver", e.message)
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
            Log.d("SMSReceiver", "Le fichier JSON a été supprimé : $cheminFichier")
        }
    }

    /**
     * Cette fonction permet d'ajouter des données dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et des données.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param donnees Ce paramètre est les données à ajouter dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param nomObjetJson Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutDonneesJSON(contexte: Context?, cheminFichier: String, nomObjetJson: String, donnees: List<String?>) {
        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJSON(contexte, cheminFichier, nomObjetJson)
            return
        }

        try {
            val contenuJson = fichier.readText()
            val json = JSONObject(contenuJson)

            val jsonArray = JSONArray()
            for (donnee in donnees) {
                donnee?.let { jsonArray.put(it) }
            }

            json.put(nomObjetJson, jsonArray)

            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(json.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Modification Fichier JSON sauvegardé avec succès : $cheminFichier")
        } catch (e: ReadWhiteListException) {
            Log.d("SMSReceiver", e.message)
        }
    }

    /**
     * Cette fonction permet de supprimer des données dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et des données.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param donnees Ce paramètre est les données à supprimer dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param nomObjetJson Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun supressionDonneesJSON(contexte: Context?, cheminFichier: String, nomObjetJson: String, donnees: List<String?>) {
        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJSON(contexte, cheminFichier, nomObjetJson)
            return
        }

        val objetJson = JSONObject()
        val valeurs = objetJson[nomObjetJson] as JSONArray
        for (donnee in donnees) {
            for (i in 0 until valeurs.length()) {
                if (valeurs[i] == donnee) {
                    valeurs.remove(i)
                }
            }
        }

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Modification Fichier JSON sauvegardé avec succès : $cheminFichier")
        } catch (e: WriteWhiteListException) {
            Log.d("SMSReceiver", e.message)
        }
    }

    /**
     * Cette fonction permet d'ajouter un objet Message dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et de l'objet Message.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param nomObjetJson Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutModelJSON(contexte: Context?, cheminFichier: String, nomObjetJson: String, message: Message) {
        val jackson = ObjectMapper().registerModule(KotlinModule())
        val messageSerialise = jackson.writeValueAsString(message)
        Log.d("SMSReceiver", "messageSerialise : $messageSerialise")

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJSON(contexte, cheminFichier, nomObjetJson)
            return
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val listeMessage : JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            json?.getJSONArray(nomObjetJson)
        }
        listeMessage?.put(messageSerialise)
        Log.d("SMSReceiver", "listeMessage : $listeMessage")

        val objetJson = JSONObject()
        objetJson.put(nomObjetJson, listeMessage)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Modification Fichier JSON sauvegardé avec succès : $cheminFichier")
        } catch (e: WriteWhiteListException) {
            Log.d("SMSReceiver", e.message)
        }
    }

    /**
     * Cette fonction permet de supprimer un objet Message dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et de l'objet Message.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à supprimer dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param nomObjetJson Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun supressionModelJSON(contexte: Context?, cheminFichier: String, nomObjetJson: String, message: Message) {
        val jackson = ObjectMapper().registerModule(KotlinModule())
        val messageSerialise = jackson.writeValueAsString(message)

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJSON(contexte, cheminFichier, nomObjetJson)
            return
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val listeMessage : JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            json?.getJSONArray(nomObjetJson)
        }

        for (i in 0 until listeMessage!!.length()) {
            if (listeMessage[i] == messageSerialise) {
                listeMessage.remove(i)
            }
        }
        Log.d("SMSReceiver", "listeMessage : $listeMessage")

        val objetJson = JSONObject()
        objetJson.put(nomObjetJson, listeMessage)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Modification Fichier JSON sauvegardé avec succès : $cheminFichier")
        } catch (e: WriteWhiteListException) {
            Log.d("SMSReceiver", e.message)
        }
    }

    /**
     * Cette fonction permet de charger le contenu d'un fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @return Cette fonction retourne le contenu du fichier JSON.
     */
    fun chargementJSON(context: Context?, cheminFichier: String): JSONObject {
        var objetJson = JSONObject()

        try {
            val fluxEntree: FileInputStream? = context?.openFileInput(cheminFichier)
            if (fluxEntree != null) {
                val jsonStr = fluxEntree.bufferedReader().use { it.readText() }
                objetJson = JSONObject(jsonStr)

                Log.d("SMSReceiver", "JSON chargé avec succès : $cheminFichier")
            }
        } catch (e: ReadWhiteListException) {
            Log.d("SMSReceiver", e.message)
        }

        return objetJson
    }
}