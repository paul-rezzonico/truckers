package com.unilim.iut.truckers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileInputStream

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

                        val whitelist = loadWhitelistFromJson(context)

                        if (sender?.let { isNumberInWhitelist(it, whitelist.toSet()) } == true) {
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

    private fun loadWhitelistFromJson(context: Context?): List<String> {
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

                Log.d("SMSReceiver", "Whitelist chargée avec succès : $whitelist")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return whitelist
    }


    private fun isNumberInWhitelist(number: String, whitelist: Set<String>): Boolean {
        return whitelist.contains(number)
    }
}
