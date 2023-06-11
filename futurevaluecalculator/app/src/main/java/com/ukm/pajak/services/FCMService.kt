package com.ukm.pajak.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ukm.pajak.tools.SharedPreferencesTools

class FCMService : FirebaseMessagingService() {

    val TAG = "FCMService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            if (true) {
                scheduleJob()
            } else {
                handleNow()
            }
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message notification body: ${it.body}")


        }

    }


    override fun onNewToken(token: String) {
        //super.onNewToken(token)

        SharedPreferencesTools.saveData(applicationContext,"tokenpush", token)
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task done")
    }

    private fun scheduleJob() {
        TODO("Not yet implemented")
    }
}

