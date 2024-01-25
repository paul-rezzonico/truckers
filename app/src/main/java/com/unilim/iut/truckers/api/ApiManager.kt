package com.unilim.iut.truckers.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.unilim.iut.truckers.controleur.MessageControleur
import com.unilim.iut.truckers.modele.MessageEnvelope
import kotlinx.coroutines.awaitAll
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiManager(
    private val contexte: Context
) {

    private val client = OkHttpClient()
    private val messageControleur = MessageControleur()

    fun envoyerMessages(messageEnvelope: MessageEnvelope, cheminURL: String) {

        val json = Gson().toJson(messageEnvelope)
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(ApiConfig.buildApiUrl(cheminURL))
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val responseBody = response.body?.string() ?: "Pas de réponse"
                Log.d("TruckerService", "Erreur $cheminURL : $responseBody")
                envoyerMessages(messageEnvelope, cheminURL)
            } else {
                Log.d("TruckerService", "Envoi de ${ApiConfig.buildApiUrl(cheminURL)} réussie : ${response.code}")
                messageControleur.supprimerMessagesApresApi(contexte, messageEnvelope.idTelephone, messageEnvelope.messages)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TruckerService", e.message.toString())
        }
    }

    fun recevoirMessages(path: String): String {
        val request = Request.Builder()
            .url(ApiConfig.buildApiUrl(path))
            .get()
            .build()

        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val responseBody = response.body?.string() ?: "Pas de réponse"
                Log.d("TruckerService", "Erreur $path : $responseBody")
                return ""
            }
            return response.body?.string() ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TruckerService", e.message.toString())
        }
        return ""
    }
}
