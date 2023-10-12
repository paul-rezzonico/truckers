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
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationListeBlanche(contexte: Context?) {
        val cheminFichier = "whitelist.json"

        val fichier = File(contexte?.filesDir, cheminFichier)
        if (fichier.exists()) {
            Log.d("SMSReceiver", "Le fichier JSON existe déjà : $cheminFichier")
            return
        }

        val whitelist = listOf(
            PhoneNumber("0987654321").phoneNumber,
            PhoneNumber("0555555555").phoneNumber
        )

        val objetJson = JSONObject()
        objetJson.put("numero_admin", PhoneNumber("0123456789").phoneNumber)
        objetJson.put("whitelist", JSONArray(whitelist))

        try {
            val fluxSortie: FileOutputStream? =
                contexte?.openFileOutput(cheminFichier, Context.MODE_PRIVATE)

            fluxSortie?.write(objetJson.toString(4).toByteArray())
            fluxSortie?.close()

            Log.d("SMSReceiver", "Fichier JSON WhiteList sauvegardé avec succès : $cheminFichier")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Cette fonction permet de charger le contnu d'un fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne le numéro de téléphone de l'administrateur.
     */
    fun chargementJson(context: Context?): JSONObject {
        val filePath = "whitelist.json"
        var jsonObject = JSONObject()

        try {
            val inputStream: FileInputStream? = context?.openFileInput(filePath)
            if (inputStream != null) {
                val jsonStr = inputStream.bufferedReader().use { it.readText() }
                jsonObject = JSONObject(jsonStr)

                Log.d("SMSReceiver", "JSON chargé avec succès")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("SMSReceiver", "Erreur lors du chargement du JSON")
        }

        return jsonObject
    }

    /**
     * Cette fonction permet de charger une liste de numéros de téléphone dépendant du type d'utilisateur.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de numéros de téléphone.
     */
    fun chargementListeBlanche(context: Context?, admin: Boolean): MutableList<String> {
        val jsonObject = chargementJson(context)
        val whitelist = mutableListOf<String>()

        if (admin) {
            val numeroAdmin = jsonObject.getString("numero_admin")
            whitelist.add(numeroAdmin)
            return whitelist
        } else {
            try {
                val jsonArray = jsonObject.getJSONArray("whitelist")

                for (i in 0 until jsonArray.length()) {
                    val phoneNumber = jsonArray.getString(i)
                    whitelist.add(phoneNumber)
                }

                Log.d("SMSReceiver", "Whitelist chargée avec succès")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("SMSReceiver", "Erreur lors du chargement de la whitelist")
            }

            return whitelist
        }
    }

    /**
     * Cette fonction permet de vérifier si un numéro de téléphone est autorisé ou non.
     *
     * @param numero Ce paramètre est le numéro de téléphone à vérifier.
     * @param listeBlanche Ce paramètre est la liste des numéros de téléphone autorisés.
     * @return Cette fonction retourne un booléen indiquant si le numéro de téléphone est autorisé.
     */
    fun numeroDansLaListeBlanche(numero: PhoneNumber?, listeBlanche: Set<String>): Boolean {
        return listeBlanche.contains(numero?.phoneNumber)
    }

    /**
     * Cette fonction permet de vérifier si un numéro de téléphone est celui de l'administrateur.
     *
     * @param numero Ce paramètre est le numéro de téléphone à vérifier.
     * @param numeroAdmin Ce paramètre est le numéro de téléphone de l'administrateur.
     * @return Cette fonction retourne un booléen indiquant si le numéro de téléphone est celui de l'administrateur.
     */
    fun numeroAdministrateur(numero: PhoneNumber?, numeroAdmin: MutableList<String>): Boolean {
        return numeroAdmin.contains(numero?.phoneNumber)
    }
}