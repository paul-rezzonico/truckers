package com.unilim.iut.truckers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.unilim.iut.truckers.model.PhoneNumber
import org.json.JSONObject
import java.io.FileInputStream

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in messages) {
                    val sender = message.originatingAddress?.let { PhoneNumber(it) }
                    val messageBody = message.messageBody

                    val whitelist = loadWhitelistFromJson(context)

                    if (isNumberInWhitelist(sender, whitelist.toSet())) {
                        Log.d("SMSReceiver", "SMS autorisé")
                        Log.d("SMSReceiver", "SMS reçu de $sender : $messageBody")
                    } else {
                        Log.d("SMSReceiver", "SMS non autorisé")
                    }
                }
            }
        }
    }

    private fun loadWhitelistFromJson(context: Context?): MutableList<String> {
        val filePath = "whitelist.json"
        val whitelist = mutableListOf<String>()

        try {
            val inputStream: FileInputStream? = context?.openFileInput(filePath)
            if (inputStream != null) {
                val jsonStr = inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonStr)
                val jsonArray = jsonObject.getJSONArray("whitelist")

                for (i in 0 until jsonArray.length()) {
                    val phoneNumber = jsonArray.getString(i)
                    whitelist.add(phoneNumber)
                }

                Log.d("SMSReceiver", "Whitelist chargée avec succès")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return whitelist
    }


    private fun isNumberInWhitelist(number: PhoneNumber?, whitelist: Set<String>): Boolean {
        return whitelist.contains(number.toString())
    }
}
