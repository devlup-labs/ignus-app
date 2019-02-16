package org.ignus.ui.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.ignus.R
import org.ignus.db.models.Workshop
import org.ignus.utils.formatDate
import org.ignus.utils.formatTime
import org.ignus.utils.openGoogleMaps
import org.ignus.utils.openURL

class WorkshopListAdapter(private val activity: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<Workshop> = emptyList()
    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.no_adapter_item, parent, false)
            MyViewHolder2(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.event_list_card_1, parent, false)
            MyViewHolder(view)
        }
    }

    override fun getItemCount() = if (list.isEmpty()) 1 else list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list.isNotEmpty()) (holder as MyViewHolder).bindData(list[position])
    }

    fun setList(list: List<Workshop>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.isEmpty()) 0 else 1
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val parent: ConstraintLayout = view.findViewById(R.id.parentLayout)
        private val title: TextView = view.findViewById(R.id.title)
        private val notify: ImageView = view.findViewById(R.id.notify)
        private val teamDetails: TextView = view.findViewById(R.id.team_details)
        private val locationLayout: ConstraintLayout = view.findViewById(R.id.location_layout)
        private val location: TextView = view.findViewById(R.id.location)
        private val time: TextView = view.findViewById(R.id.time)

        fun bindData(workshop: Workshop) {
            title.text = workshop.name
            location.text = workshop.location?.name ?: "LHC-000"
            time.text = if (!workshop.end_time.isNullOrBlank()) activity.getString(
                R.string.workshop_time,
                workshop.start_time?.formatTime,
                workshop.end_time.formatTime
            )
            else workshop.start_time?.formatDate

            if (checkNotify(workshop.id)) notify.setColorFilter(ContextCompat.getColor(activity, R.color.notify))
            else notify.setColorFilter(Color.GRAY)

            if (workshop.type?.equals("A", true) == true) {
                teamDetails.text = activity.getString(R.string.workshop_day, 1)
                locationLayout.background = activity.getDrawable(R.drawable.workshop_bg_a)
            } else if (workshop.type?.equals("B", true) == true) {
                teamDetails.text = activity.getString(R.string.workshop_day, 2)
                locationLayout.background = activity.getDrawable(R.drawable.workshop_bg_b)
            }

            notify.setOnClickListener { notify(workshop.id) }
            parent.setOnClickListener { showDetails(workshop) }
            locationLayout.setOnClickListener { openGoogleMaps(workshop.location) }

        }

        private fun checkNotify(string: String): Boolean {
            return sp.getBoolean("notify-$string", false)
        }

        private fun notify(string: String) {
            if (checkNotify(string)) {
                notify.setColorFilter(Color.GRAY)
                sp.edit().putBoolean("notify-$string", false).apply()
            } else {
                notify.setColorFilter(ContextCompat.getColor(activity, R.color.notify))
                val shake = AnimationUtils.loadAnimation(activity, R.anim.shakeanim)
                notify.startAnimation(shake)
                sp.edit().putBoolean("notify-$string", true).apply()
            }
        }

        private fun showDetails(workshop: Workshop) {

            val builder = AlertDialog.Builder(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.workshop_details_card, null)
            builder.setView(view)
            val dialog = builder.create()

            val title = view.findViewById<TextView>(R.id.title)
            val date = view.findViewById<TextView>(R.id.date)
            val details = view.findViewById<TextView>(R.id.details)
            val positiveBtn = view.findViewById<Button>(R.id.positive_btn)
            val neutralBtn = view.findViewById<Button>(R.id.neutral_btn)
            val externalLink = view.findViewById<ImageView>(R.id.external_link)

            details.movementMethod = ScrollingMovementMethod()

            title.text = workshop.name
            date.text = if (!workshop.end_time.isNullOrBlank()) activity.getString(
                R.string.workshop_time,
                workshop.start_time?.formatDate,
                workshop.end_time.formatDate
            ) else workshop.start_time?.formatDate

            details.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(workshop.about, Html.FROM_HTML_MODE_LEGACY)?.trim()
            else Html.fromHtml(workshop.about)?.trim()

            positiveBtn.setOnClickListener {
                showOrganisers(workshop)
                dialog.dismiss()
            }

            if (!workshop.pdf.isNullOrBlank()) {
                neutralBtn.visibility = View.VISIBLE
                neutralBtn.setOnClickListener {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(workshop.pdf)))
                }
            } else
                neutralBtn.visibility = View.GONE


            externalLink.setOnClickListener { openURL("https://ignus.org/workshops/") }

            dialog.apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
        }

        private fun showOrganisers(workshop: Workshop) {

            val builder = AlertDialog.Builder(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.organisers_list_dialog, null)
            builder.setView(view)

            val title: TextView = view.findViewById(R.id.title)
            val recyclerView: RecyclerView = view.findViewById(R.id.organisersListRecyclerView)

            title.text = activity.getString(R.string.organisers)
            recyclerView.adapter = OrganiserListAdapter(workshop.organiser_list ?: return)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    class MyViewHolder2(view: View) : RecyclerView.ViewHolder(view)
}