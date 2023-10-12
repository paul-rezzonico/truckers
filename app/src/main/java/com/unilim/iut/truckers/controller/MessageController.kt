package com.unilim.iut.truckers.controller

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.unilim.iut.truckers.model.Message
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MessageController {

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste d'objet Message.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJsonBonMessage(contexte: Context?) {
        val cheminFichier = "RightMessage.json"

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $cheminFichier")
            return
        }

        val objetJson = JSONObject()
        objetJson.put("messages", null)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Fichier JSON RightMessage sauvegardé avec succès : $cheminFichier")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Cette fonction permet d'ajouter un message dans le fichier JSON contenant une liste d'objet Message qui sont ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutMessageDansJsonBonMessage(contexte: Context?, message: Message) {
        val jackson = ObjectMapper().registerModule(KotlinModule())
        val messageSerialise = jackson.writeValueAsString(message)
        Log.d("SMSReceiver", "messageSerialise : $messageSerialise")

        val cheminFichier = "RightMessage.json"

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJsonBonMessage(contexte)
            return
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val listeMessage : JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            json?.getJSONArray("messages")
        }
        listeMessage?.put(messageSerialise)
        Log.d("SMSReceiver", "listeMessage : $listeMessage")

        val objetJson = JSONObject()
        objetJson.put("messages", listeMessage)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Fichier JSON RightMessage sauvegardé avec succès : $cheminFichier")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationJsonMauvaisMessage(contexte: Context?) {
        val cheminFichier = "WrongMessage.json"

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $cheminFichier")
            return
        }

        val objetJson = JSONObject()
        objetJson.put("messages", null)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Fichier JSON WrongMessage sauvegardé avec succès : $cheminFichier")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Cette fonction permet d'ajouter un message dans le fichier JSON contenant une liste d'objet Message qui ne sont pas ceux recherchés.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @param message Ce paramètre est l'objet Message à ajouter dans le fichier JSON.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutMessageDansMauvaisJsonMessage(contexte: Context?, message: Message) {
        val jackson = ObjectMapper().registerModule(KotlinModule())
        val messageSerialise = jackson.writeValueAsString(message)

        val cheminFichier = "WrongMessage.json"

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (!fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON n'existe pas : $cheminFichier")
            Log.d("SMSReceiver", "Création du fichier JSON : $cheminFichier")
            creationJsonMauvaisMessage(contexte)
            return
        }

        val fluxEntree: FileInputStream? = contexte?.openFileInput(cheminFichier)
        val json = fluxEntree?.bufferedReader().use { it?.readText() }?.let { JSONObject(it) }

        val listeMessage : JSONArray? = if (json.toString() == "{}") {
            JSONArray()
        } else {
            json?.getJSONArray("messages")
        }
        listeMessage?.put(messageSerialise)

        val objetJson = JSONObject()
        objetJson.put("messages", listeMessage)

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d(
                "SMSReceiver",
                "Fichier JSON WrongMessage sauvegardé avec succès : $cheminFichier"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}