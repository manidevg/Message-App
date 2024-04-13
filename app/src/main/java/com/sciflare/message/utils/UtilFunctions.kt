package com.sciflare.message.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Patterns
import com.sciflare.message.model.SmsModel

object UtilFunctions {

    fun readSms(context: Context, smsType: String): ArrayList<SmsModel> {
        var list = ArrayList<SmsModel>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val smsAddress =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val message = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
                val decryptedText = SecurityUtils.decrypt(message)
                if (decryptedText.contains(context.packageName) && type == smsType) {
                    list.add(
                        SmsModel(
                            receiverNumber = smsAddress,
                            message = decryptedText.replace("|${context.packageName}|", "")
                        )
                    )
                }
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return list
    }

    fun sendSMS(context: Context, phoneNumber: String, message: String) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent("SMS_SENT"),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null)
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return Patterns.PHONE.matcher(phoneNumber).matches()
    }

}