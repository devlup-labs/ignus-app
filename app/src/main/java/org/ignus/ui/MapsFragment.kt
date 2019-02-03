package org.ignus.ui


import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
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
import org.ignus.config.Map
import org.ignus.db.models.Venue
import org.ignus.utils.px


class MapsFragment : Fragment(), OnMapReadyCallback {

    private val iitJBound = LatLngBounds(LatLng(26.456988, 73.106086), LatLng(26.488886, 73.124908))
    private val venues: ArrayList<Venue> by lazy { ArrayList<Venue>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        venues.add(
            Venue(
                "LHB",
                "SBI-SBI is bond",
                LatLng(26.47294, 73.1138),
                Map.EVENTS,
                R.drawable.ic_mail
            )
        )
        venues.add(
            Venue(
                "LHB-Girls and Boys Toilet",
                "MutLo",
                LatLng(26.47293, 73.11402)
            )
        )
        venues.add(
            Venue(
                "B1-Hostel",
                "So Jao",
                LatLng(26.472712787811197, 73.11524460598935)
            )
        )
        venues.add(
            Venue(
                "LHB",
                "Whatever",
                LatLng(26.47204, 73.1148)
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

    override fun onMapReady(map: GoogleMap?) {
        map ?: return
        try {
            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.style_josn))
            if (!success) Log.e("suthar", "Style parsing failed.")
        } catch (e: Resources.NotFoundException) {
            Log.e("suthar", "Can't find style. Error: ", e)
        }

        if (ContextCompat.checkSelfPermission(
                context ?: return,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) map.isMyLocationEnabled = true
        else requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 102
        )

        // val position = CameraPosition(venues[0].location, 15.0f, 45f, 0f)
        // map.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        // map?.setLatLngBoundsForCameraTarget(iitJBound)
        // map?.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        map.isBuildingsEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(venues[0].location, 15.5f))

        showMarkers(map, Map.UNFILTERED)

        Handler().postDelayed({ showMarkers(map, Map.EVENTS) }, 5000)
    }

    private fun showMarkers(map: GoogleMap, filter: Map) {

        map.clear()

        for (venue in venues) {

            if (filter != Map.UNFILTERED && venue.type != filter) continue

            val drawable = ContextCompat.getDrawable(context ?: return, venue.icon) ?: return
            DrawableCompat.setTint(drawable, Color.parseColor(venue.tint))
            val icon = getMarkerIconFromDrawable(drawable)

            map.addMarker(
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
        val bitmap = Bitmap.createBitmap(24.px, 24.px, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, 24.px, 24.px)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
