package com.unilim.iut.truckers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReceveurDeDemarrage : BroadcastReceiver() {
    override fun onReceive(contexte: Context?, intention: Intent?) {
        if (intention?.action == Intent.ACTION_BOOT_COMPLETED) {
            val intentionDeLancement = contexte?.packageManager?.getLaunchIntentForPackage(contexte.packageName)
            intentionDeLancement?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                contexte.startActivity(it)
            }
        }
    }
}
