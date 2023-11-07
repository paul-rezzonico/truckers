package com.unilim.iut.truckers.controller

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.unilim.iut.truckers.exception.ReadWhiteListException
import com.unilim.iut.truckers.exception.WriteWhiteListException
import com.unilim.iut.truckers.facade.IFacadeDePersistence
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class JsonController : IFacadeDePersistence{
    private val jackson = ObjectMapper().registerModule(KotlinModule())


    fun creationJSON(contexte: Context?, cheminFichier: String, champs: String) {
        val fichier = File(contexte?.filesDir, cheminFichier)
        if (fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $cheminFichier")
            return
        }

        val objetJson = JSONObject()
        objetJson.put(champs, null)

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
     * Cette fonction permet d'ajouter un objet Message dans un fichier JSON en fonction du chemin du fichier, du nom de l'objet JSON et de l'objet Message.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param champs Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    override fun sauvegarder(contexte: Context?, cheminFichier: String, champs: String, message: Any) {

        val messageSerialise = jackson.writeValueAsString(message)
        Log.d("SMSReceiver", "messageSerialise : $messageSerialise")

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJSON(contexte, cheminFichier, champs)
            return
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val listeMessage : JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            json?.getJSONArray(champs)
        }
        listeMessage?.put(messageSerialise)

        val objetJson = JSONObject()
        objetJson.put(champs, listeMessage)

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
     * @param donnees Ce paramètre est l'objet Message à supprimer dans le fichier JSON.
     * @param cheminFichier Ce paramètre est le chemin du fichier JSON.
     * @param champs Ce paramètre est le nom de l'objet JSON.
     * @return Cette fonction ne retourne rien.
     */
    override fun supprimer(
        contexte: Context?,
        cheminFichier: String,
        champs: String,
        donnees: Any
    ) {
        val jackson = ObjectMapper().registerModule(KotlinModule())
        val messageSerialise = jackson.writeValueAsString(donnees)

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJSON(contexte, cheminFichier, champs)
            return
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val listeMessage : JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            json?.getJSONArray(champs)
        }

        for (i in 0 until listeMessage!!.length()) {
            if (listeMessage[i] == messageSerialise) {
                listeMessage.remove(i)
            }
        }

        val objetJson = JSONObject()
        objetJson.put(champs, listeMessage)

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
    override fun charger(context: Context?, cheminFichier: String): JSONObject {
        var objetJson = JSONObject()

        try {
            val fluxEntree: FileInputStream? = context?.openFileInput(cheminFichier)
            if (fluxEntree != null) {
                val jsonStr = fluxEntree.bufferedReader().use { it.readText() }
                objetJson = JSONObject(jsonStr)
            }
        } catch (e: ReadWhiteListException) {
            Log.d("SMSReceiver", e.message)
        }

        return objetJson
    }
}
