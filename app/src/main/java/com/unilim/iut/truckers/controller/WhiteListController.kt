package com.unilim.iut.truckers.controller

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.model.PhoneNumber
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class WhiteListController {

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de numéros de téléphone.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun createWhitelistFromJson(context: Context?) {
        val filePath = "whitelist.json"

        val file = File(context?.filesDir, filePath)
        if (file.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $filePath")
            return
        }

        val whitelist = listOf(PhoneNumber("0123456789").toString(), PhoneNumber("0987654321").toString(), PhoneNumber("0555555555").toString())

        val jsonObject = JSONObject()
        jsonObject.put("whitelist", JSONArray(whitelist))

        try {
            val outputStream: FileOutputStream? = context?.openFileOutput(filePath, Context.MODE_PRIVATE)

            outputStream?.write(jsonObject.toString(4).toByteArray())
            outputStream?.close()

            Log.d("SMSReceiver","Fichier JSON sauvegardé avec succès : $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Cette fonction permet de charger une liste de numéros de téléphone à partir d'un fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de numéros de téléphone.
     */
    fun loadWhitelistFromJson(context: Context?): MutableList<String> {
        val filePath = "whitelist.json"
        val whitelist = mutableListOf<String>()

        try {
            val inputStream: FileInputStream? = context?.openFileInput(filePath)
            if (inputStream != null) {
                val jsonStr = inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonStr)
                val jsonArray = jsonObject.getJSONArray("whitelist")

                for (i in 0 until jsonArray.length()) {
                    val phoneNumber = jsonArray.getString(i)
                    whitelist.add(phoneNumber)
                }

                Log.d("SMSReceiver", "Whitelist chargée avec succès")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("SMSReceiver", "Erreur lors du chargement de la whitelist")
        }

        return whitelist
    }

    /**
     * Cette fonction permet de vérifier si un numéro de téléphone est autorisé ou non.
     *
     * @param number Ce paramètre est le numéro de téléphone à vérifier.
     * @param whitelist Ce paramètre est la liste des numéros de téléphone autorisés.
     * @return Cette fonction retourne un booléen indiquant si le numéro de téléphone est autorisé.
     */
    fun isNumberInWhitelist(number: PhoneNumber?, whitelist: Set<String>): Boolean {
        return whitelist.contains(number?.phoneNumber)
    }
}