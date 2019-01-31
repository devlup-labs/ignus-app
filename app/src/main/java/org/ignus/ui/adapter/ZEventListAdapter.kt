package org.ignus.ui.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.ignus.App
import org.ignus.R
import org.ignus.db.models.Event
import org.ignus.db.models.EventCategory
import org.ignus.db.models.Location

class ZEventListAdapter(private val activity: Activity, private val eventCategory: EventCategory) :
    RecyclerView.Adapter<ZEventListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_list_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = eventCategory.events?.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(activity, eventCategory.events?.get(position))
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val cover: ImageView = view.findViewById(R.id.imageView)
        private val title: TextView = view.findViewById(R.id.title)
        private val date: TextView = view.findViewById(R.id.date)
        private val teamSize: TextView = view.findViewById(R.id.team_size)
        private val location: TextView = view.findViewById(R.id.location)

        fun bindView(activity: Activity, event: Event?) {

            event ?: return
            Glide.with(cover)
                .load(event.cover_url)
                .apply(
                    RequestOptions()
                        .placeholder(ColorDrawable(Color.BLACK))
                        .error(R.drawable.placeholder)
                )
                .into(cover)

            title.text = event.name
            date.text = event.date_time
            teamSize.text = activity.getString(R.string.team_size, event.min_team_size, event.max_team_size)
            location.text = event.location?.name

            location.setOnClickListener { openGoogleMaps(event.location) }
        }

        private fun openGoogleMaps(location: Location?) {
            if (location == null) {
                Toast.makeText(App.instance, "Location not available!", Toast.LENGTH_SHORT).show()
                return
            }
            val pos = location.longitude + "," + location.latitude
            val uri = "https://www.google.com/maps/dir/?api=1&destination=$pos&travelmode=walking"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            App.instance.startActivity(intent)
        }
    }
}