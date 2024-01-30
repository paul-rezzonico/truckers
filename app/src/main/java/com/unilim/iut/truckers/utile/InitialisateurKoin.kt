package com.unilim.iut.truckers.utile

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InitialisateurKoin(private val context: Context) {

    fun setupKoin() {
        startKoin {
            androidContext(context)
            modules(syncModule)
        }
    }
}