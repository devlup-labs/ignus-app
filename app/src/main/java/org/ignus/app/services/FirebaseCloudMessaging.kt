package org.ignus.app.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseCloudMessaging : FirebaseMessagingService() {

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.d("suthar, FCM", p0)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
    }
}