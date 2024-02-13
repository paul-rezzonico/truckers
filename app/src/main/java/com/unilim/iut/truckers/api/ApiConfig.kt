package com.unilim.iut.truckers.api

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object ApiConfig {

    private val BASE_URL: HttpUrl = HttpUrl.Builder()
        .scheme("http")
        .host("10.0.2.2")
        .port(8080)
        .build()

    fun buildApiUrl(path: String): HttpUrl {
        return (BASE_URL.toString() + path).toHttpUrlOrNull() ?: throw Exception("Impossible de construire l'URL")
    }
}

