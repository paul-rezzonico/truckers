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

    /**
     * Cette fonction permet d'ajouter un numéro de téléphone dans le fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @param admin Ce paramètre est un booléen indiquant si le numéro de téléphone est celui de l'administrateur.
     * @param numero Ce paramètre est le numéro de téléphone à ajouter.
     * @return Cette fonction ne retourne rien.
     */
    fun ajoutNumeroJSON(context: Context?, admin: Boolean, numero: List<PhoneNumber>) {
        for (num in numero) {
            jsonController.sauvegarder(context, "ListeBlanche.json", if (admin) "numero_admin" else "liste_blanche", num)
        }
    }

    /**
     * Cette fonction permet de supprimer un numéro de téléphone dans le fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @param admin Ce paramètre est un booléen indiquant si le numéro de téléphone est celui de l'administrateur.
     * @param numero Ce paramètre est le numéro de téléphone à supprimer.
     * @return Cette fonction ne retourne rien.
     */
    fun supressionNumeroJSON(context: Context?, admin: Boolean, numero: PhoneNumber) {
        return jsonController.supprimer(context, "ListeBlanche.json", if (admin) "numero_admin" else "liste_blanche", numero.phoneNumber)
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