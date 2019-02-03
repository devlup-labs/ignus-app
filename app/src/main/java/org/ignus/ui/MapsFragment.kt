package org.ignus.ui


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.ignus.R
import org.ignus.db.models.Venue
import org.ignus.utils.px


class MapsFragment : Fragment(), OnMapReadyCallback {

    private val venues: ArrayList<Venue> by lazy { ArrayList<Venue>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        venues.add(
            Venue(
                "LHB",
                "SBI-SBI is bond",
                LatLng(26.47294, 73.1138),
                R.drawable.ic_mail
            )
        )
        venues.add(
            Venue(
                "LHB-Girls and Boys Toilet",
                "MutLo",
                LatLng(26.47293, 73.11402),
                R.drawable.ic_call
            )
        )
        venues.add(
            Venue(
                "B1-Hostel",
                "So Jao",
                LatLng(26.472712787811197, 73.11524460598935),
                R.drawable.ic_cultural
            )
        )
        venues.add(
            Venue(
                "LHB",
                "Whatever",
                LatLng(26.47204, 73.1148),
                R.drawable.ic_gmap
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMap()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        Log.d("suthar", "onMapReady")
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.style_josn))

            if (success != true) Log.e("suthar", "Style parsing failed.")

        } catch (e: Resources.NotFoundException) {
            Log.e("suthar", "Can't find style. Error: ", e)
        }

        // Position the map's camera near LHB.
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(venues[0].location))
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(15.0f))

        for (venue in venues) {

            val drawable = ContextCompat.getDrawable(context ?: return, venue.icon) ?: return
            DrawableCompat.setTint(drawable, Color.parseColor(venue.tint))
            val icon = getMarkerIconFromDrawable(drawable)

            googleMap?.addMarker(
                MarkerOptions()
                    .position(venue.location)
                    .title(venue.name)
                    .snippet(venue.snippet)
                    .draggable(false)
                    .icon(icon)

            )
        }
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        // drawable.colorFilter = PorterDuffColorFilter(0xffffff, PorterDuff.Mode.MULTIPLY)
        val bitmap = Bitmap.createBitmap(24.px, 24.px, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, 24.px, 24.px)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
