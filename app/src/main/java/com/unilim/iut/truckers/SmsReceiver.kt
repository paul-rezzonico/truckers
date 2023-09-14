package com.unilim.iut.truckers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle: Bundle? = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>?
                if (pdus != null) {
                    for (pdu in pdus) {
                        val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                        val sender = smsMessage.originatingAddress
                        val messageBody = smsMessage.messageBody

                        // Faites ce que vous voulez avec les données du SMS (sender et messageBody)
                        Toast.makeText(context, "SMS reçu de $sender : $messageBody", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}


