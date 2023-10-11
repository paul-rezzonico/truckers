package com.unilim.iut.truckers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.unilim.iut.truckers.model.PhoneNumber
import com.unilim.iut.truckers.controller.WhiteListController

class SmsReceiver : BroadcastReceiver() {

    private val whiteListController = WhiteListController();

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in messages) {
                    val sender = message.originatingAddress?.let { PhoneNumber(it) }
                    val messageBody = message.messageBody

                    val whitelist = whiteListController.loadWhitelistFromJson(context)

                    if (whiteListController.isNumberInWhitelist(sender, whitelist.toSet())) {
                        Log.d("SMSReceiver", "SMS autorisé")
                        Log.d("SMSReceiver", "SMS reçu de $sender : $messageBody")
                    } else {
                        Log.d("SMSReceiver", "SMS non autorisé")
                    }
                }
            }
        }
    }
}
