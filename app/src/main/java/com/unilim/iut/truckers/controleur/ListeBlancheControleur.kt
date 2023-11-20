package com.unilim.iut.truckers.controleur

import android.content.Context
import android.util.Log
import com.unilim.iut.truckers.exception.LectureListeBlancheException
import org.json.JSONObject

class ListeBlancheControleur {

    private val jsonControleur = JsonControleur()
    private val logcatControleur = LogcatControleur()

    /**
     * Cette fonction permet de créer un fichier JSON contenant une liste de numéros de téléphone.
     *
     * @param contexte Ce paramètre est le contexte de l'application.
     * @return Cette fonction ne retourne rien.
     */
    fun creationListeBlanche(contexte: Context?) {
        jsonControleur.creationJSON(contexte, "ListeBlanche.json", "liste_blanche")
    }

    /**
     * Cette fonction permet de charger le contnu d'un fichier JSON.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne le numéro de téléphone de l'administrateur.
     */
    fun chargementJson(context: Context?): JSONObject {
        return jsonControleur.charger(context, "ListeBlanche.json")
    }

    /**
     * Cette fonction permet de charger une liste de numéros de téléphone dépendant du type d'utilisateur.
     *
     * @param context Ce paramètre est le contexte de l'application.
     * @return Cette fonction retourne une liste de numéros de téléphone.
     */
    fun chargementListeBlanche(context: Context?, admin: Boolean): MutableList<String> {
        val objetJson = chargementJson(context)
        val listeBlanche = mutableListOf<String>()

        if (admin && (objetJson.has("numero_admin"))) {
            val tableauJSON = objetJson.getJSONArray("numero_admin")
            if (tableauJSON.length() > 0) {
                val numeroAdmin = tableauJSON.getString(0)
                listeBlanche.add(numeroAdmin)
            }
        } else if (objetJson.has("liste_blanche")) {
            try {
                val tableauJSON = objetJson.getJSONArray("liste_blanche")

                for (i in 0 until tableauJSON.length()) {
                    val numeroTelephone = tableauJSON.getString(i)
                    listeBlanche.add(numeroTelephone)
                }

            } catch (e: LectureListeBlancheException) {
                Log.d("TruckerService", e.message)
                logcatControleur.ecrireDansFichierLog(e.message)
            }
        }

        return listeBlanche
    }
}