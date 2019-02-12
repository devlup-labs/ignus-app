package org.ignus.utils

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.widget.Toast
import org.ignus.App
import org.ignus.db.models.Location
import java.text.SimpleDateFormat
import java.util.*

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val String.formatDate: String
    get() {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("'('EEE')' dd MMM hh:mm aa", Locale.getDefault())
        return try {
            formatter.format(parser.parse(this))
        } catch (e: Exception) {
            "Error"
        }
    }

val String.formatTime: String
    get() {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("EEE dd HH:mm", Locale.getDefault())
        return try {
            formatter.format(parser.parse(this))
        } catch (e: Exception) {
            "Error"
        }
    }

fun openGoogleMaps(location: Location?) {
    if (location == null) {
        Toast.makeText(App.instance, "Location not available!", Toast.LENGTH_SHORT).show()
        return
    }
    val pos = location.latitude + "," + location.longitude
    val uri = "https://www.google.com/maps/dir/?api=1&map_action=map&basemap=satellite&destination=$pos&travelmode=walking"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    App.instance.startActivity(intent)
}

fun openURL(url: String?) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.data = Uri.parse(url)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    try {
        App.instance.startActivity(intent)
    } catch (e: Exception) {
        Log.d("suthar", e.toString())
        Toast.makeText(App.instance, "Cannot open URL", Toast.LENGTH_SHORT).show()
    }
}