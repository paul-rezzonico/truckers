package com.unilim.iut.truckers.api

import android.util.Log
import com.google.gson.Gson
import com.unilim.iut.truckers.modele.MessageEnvelope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiManager {

    private val client = OkHttpClient()

    fun envoyerMessages(messageEnvelope: MessageEnvelope, cheminURL: String) {

        val json = Gson().toJson(messageEnvelope)
        Log.d("TruckerService", json)
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.d("TruckerService", "Envoi de ${ApiConfig.buildApiUrl(cheminURL)}")
        val request = Request.Builder()
            .url(ApiConfig.buildApiUrl(cheminURL))
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            Log.d("TruckerService", "Code Réponse $cheminURL : ${response.code}")
            if (!response.isSuccessful) {
                val responseBody = response.body?.string() ?: "Pas de réponse"
                Log.d("TruckerService", "Erreur $cheminURL : $responseBody")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TruckerService", e.message.toString())
        }
    }
}
