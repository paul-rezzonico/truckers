package com.unilim.iut.truckers.controller

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.exception.ReadWhiteListException
import com.unilim.iut.truckers.model.PhoneNumber
import org.json.JSONObject

class WhiteListController {

    private val jsonController = JsonController()

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de numéros de téléphone.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationListeBlanche(contexte: Context?) {
        jsonController.creationJSON(contexte, "ListeBlanche.json", "liste_blanche")
    }

    /**
     * Cette fonction permet de charger le contnu d'un fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne le numéro de téléphone de l'administrateur.
     */
    fun chargementJson(context: Context?): JSONObject {
        return jsonController.charger(context, "ListeBlanche.json")
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

        if (admin && (jsonObject.has("numero_admin"))) {
            val jsonArray = jsonObject.getJSONArray("numero_admin")
            if (jsonArray.length() > 0) {
                val numeroAdmin = jsonArray.getString(0)
                whitelist.add(numeroAdmin)
            }
        } else if (jsonObject.has("liste_blanche")) {
            try {
                val jsonArray = jsonObject.getJSONArray("liste_blanche")

                for (i in 0 until jsonArray.length()) {
                    val phoneNumber = jsonArray.getString(i)
                    whitelist.add(phoneNumber)
                }

            } catch (e: ReadWhiteListException) {
                Log.d("TruckerService", e.message)
            }
        }

        return whitelist
    }
}