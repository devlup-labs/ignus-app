package org.ignus.ui


import android.content.pm.PackageManager
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
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import org.ignus.R
import org.ignus.config.Map
import org.ignus.db.models.Venue
import org.ignus.utils.px


class MapsFragment : Fragment(), OnMapReadyCallback {

    private val venues: ArrayList<Venue> by lazy { ArrayList<Venue>() }
    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVenues()
        initMap()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map ?: return

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

        showMarkers(map, Map.UNFILTERED)

        // Handler().postDelayed({ showMarkers(map, Map.EVENTS) }, 5000)
    }

    private fun showMarkers(map: GoogleMap, filter: Map = Map.UNFILTERED) {

        map.clear()
        if (venues.isNotEmpty()) map.moveCamera(CameraUpdateFactory.newLatLngZoom(venues[0].location, 15.5f))

        for (venue in venues) {

            if (filter != Map.UNFILTERED && venue.type != filter) continue

            val mIcon: Int
            val tint: String

            val type = try {
                venue.type
            } catch (e: Exception) {
                Map.UNFILTERED
            }

            when (type) {
                Map.ACCOMMODATION -> {
                    mIcon = R.drawable.map_accomodation
                    tint = "#607D8B"
                }
                Map.ATM -> {
                    mIcon = R.drawable.map_atm
                    tint = "#9E9E9E"
                }
                Map.BUS -> {
                    mIcon = R.drawable.map_bus
                    tint = "#E91E63"
                }
                Map.EATERIES -> {
                    mIcon = R.drawable.map_eat
                    tint = "#FF5722"
                }
                Map.ENTRY -> {
                    mIcon = R.drawable.map_placeholder
                    tint = "#607D8B"
                }
                Map.EVENTS -> {
                    mIcon = R.drawable.map_events
                    tint = "#2196F3"
                }
                Map.HOSTELS -> {
                    mIcon = R.drawable.map_hostel
                    tint = "#009688"
                }
                Map.HOSPITAL -> {
                    mIcon = R.drawable.map_hospital
                    tint = "#D80027"
                }
                Map.WASHROOM -> {
                    tint = "#795548"
                    mIcon = R.drawable.map_washroom
                }
                else -> {
                    tint = "#000000"
                    mIcon = R.drawable.map_placeholder
                }
            }

            val drawable = ContextCompat.getDrawable(context ?: return, mIcon) ?: return
            DrawableCompat.setTint(drawable, Color.parseColor(tint))
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

    private fun loadVenues() {

        val ref = FirebaseDatabase.getInstance().getReference("map/venues")
        ref.keepSynced(true)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val gson = Gson()
                    val json = gson.toJson(snap.value)
                    val venue = gson.fromJson<Venue>(json, Venue::class.java)
                    venues.add(venue)
                }
                showMarkers(map ?: return)
            }
        })
    }
}
