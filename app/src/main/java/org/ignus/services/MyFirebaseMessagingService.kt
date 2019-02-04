package org.ignus.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.ignus.R
import org.ignus.ui.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage?.notification != null) {
            Log.d("suthar", "Message Notification Body: " + remoteMessage.notification?.body)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId = "Default"

            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification?.body))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }
            manager.notify(0, builder.build())
        }
    }
}