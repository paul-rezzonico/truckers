package com.unilim.iut.truckers.utile

import com.unilim.iut.truckers.controleur.SynchronisationControleur
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val syncModule = module {
    single { SynchronisationControleur(androidContext()) }
}