package org.ignus.utils

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.widget.Toast
import org.ignus.App
import org.ignus.db.models.Location
import java.text.SimpleDateFormat
import java.util.*

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val String.formatDate: String
    get() {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())
        val formatter = SimpleDateFormat("'('EEE')' dd MMM hh:mm aa", Locale.getDefault())
        return try {
            formatter.format(parser.parse(this))
        } catch (e: Exception) {
            "Error"
        }
    }

val String.formatTime: String
    get() {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())
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
    val pos = location.longitude + "," + location.latitude
    val uri = "https://www.google.com/maps/dir/?api=1&destination=$pos&travelmode=walking"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    App.instance.startActivity(intent)
}