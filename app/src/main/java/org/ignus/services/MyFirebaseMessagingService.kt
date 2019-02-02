package org.ignus.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        if (p0?.notification != null) {
            Log.d("suthar", "Message Notification Body: " + p0.notification?.body)
        }
    }
}