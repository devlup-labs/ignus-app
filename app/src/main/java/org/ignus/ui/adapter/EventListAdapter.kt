package org.ignus.ui.adapter

import android.annotation.SuppressLint
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
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.ignus.R
import org.ignus.db.models.Event
import org.ignus.db.models.EventCategory
import org.ignus.db.viewmodels.EventDetailsViewModel
import org.ignus.ui.EventListFragment
import org.ignus.utils.formatDate
import org.ignus.utils.openGoogleMaps

class EventListAdapter(
    private val activity: Activity,
    private val eventCategory: EventCategory,
    private val eventListFragment: EventListFragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }

    private val viewModel: EventDetailsViewModel by lazy {
        ViewModelProviders.of(eventListFragment).get(EventDetailsViewModel::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0)
            MyViewHolder0(LayoutInflater.from(parent.context).inflate(R.layout.event_list_card_0, parent, false))
        else MyViewHolder1(LayoutInflater.from(parent.context).inflate(R.layout.event_list_card_1, parent, false))

    }

    override fun getItemCount() = (eventCategory.events?.size ?: 0) + 1

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as MyViewHolder0).bindData(eventCategory.about)
            1 -> (holder as MyViewHolder1).bindData(eventCategory.events?.get(position - 1))
        }
    }


    inner class MyViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        private val parent: ConstraintLayout = view.findViewById(R.id.parentLayout)
        private val title: TextView = view.findViewById(R.id.title)
        private val notify: ImageView = view.findViewById(R.id.notify)
        private val teamDetails: TextView = view.findViewById(R.id.team_details)
        private val locationLayout: ConstraintLayout = view.findViewById(R.id.location_layout)
        private val location: TextView = view.findViewById(R.id.location)
        private val time: TextView = view.findViewById(R.id.time)

        fun bindData(event: Event?) {
            event ?: return
            title.text = event.name
            teamDetails.text = activity.getString(R.string.team_size, event.min_team_size, event.max_team_size)
            location.text = event.location?.name
            time.text = event.start_time?.formatDate

            if (checkNotify(event.unique_id)) notify.setColorFilter(ContextCompat.getColor(activity, R.color.notify))
            else notify.setColorFilter(Color.GRAY)

            notify.setOnClickListener { notify(event.unique_id) }
            parent.setOnClickListener { showDetails(event) }
            locationLayout.setOnClickListener { openGoogleMaps(event.location) }

        }

        private fun checkNotify(string: String?): Boolean {
            return sp.getBoolean("notify-$string", false)
        }

        private fun notify(string: String?) {
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

        @SuppressLint("InflateParams")
        private fun showDetails(event: Event) {

            val builder = AlertDialog.Builder(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.workshop_details_card, null)
            builder.setView(view)
            val dialog = builder.create()

            val title = view.findViewById<TextView>(R.id.title)
            val date = view.findViewById<TextView>(R.id.date)
            val details = view.findViewById<TextView>(R.id.details)
            val loadingProgressBar = view.findViewById<ContentLoadingProgressBar>(R.id.loading)
            val positiveBtn = view.findViewById<Button>(R.id.positive_btn)
            val neutralBtn = view.findViewById<Button>(R.id.neutral_btn)

            loadingProgressBar.visibility = View.VISIBLE
            details.movementMethod = ScrollingMovementMethod()

            title.text = event.name
            date.text = if (!event.end_time.isNullOrBlank()) activity.getString(
                R.string.workshop_time,
                event.start_time?.formatDate,
                event.end_time.formatDate
            ) else event.start_time?.formatDate

            viewModel.refreshEventCategories(event.unique_id ?: return)
            viewModel.eventDetails.observe(eventListFragment, Observer {

                val detailsTxt = activity.getString(R.string.event_details, it.details, it.about)

                details.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    Html.fromHtml(detailsTxt, Html.FROM_HTML_MODE_LEGACY)?.trim()
                else Html.fromHtml(detailsTxt)?.trim()

                if (!it.pdf.isNullOrBlank()) {
                    neutralBtn.visibility = View.VISIBLE
                    neutralBtn.setOnClickListener { _ ->
                        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.pdf)))
                    }
                } else
                    neutralBtn.visibility = View.GONE

            })

            viewModel.eventDetailsError.observe(eventListFragment, Observer {
                Toast.makeText(activity, "Something wrong happened!", Toast.LENGTH_LONG).show()
            })

            viewModel.getLoading().observe(eventListFragment, Observer {
                if (it) loadingProgressBar.show()
                else loadingProgressBar.hide()
            })

            positiveBtn.setOnClickListener {
                showOrganisers(event)
                dialog.dismiss()
            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

        }

        private fun showOrganisers(event: Event) {

            val builder = AlertDialog.Builder(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.organisers_list_dialog, null)
            builder.setView(view)

            val title: TextView = view.findViewById(R.id.title)
            val recyclerView: RecyclerView = view.findViewById(R.id.organisersListRecyclerView)

            title.text = event.name
            recyclerView.adapter = OrganiserListAdapter(event.organiser_list ?: return)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    class MyViewHolder0(view: View) : RecyclerView.ViewHolder(view) {

        private val details: TextView = view.findViewById(R.id.details)

        fun bindData(about: String?) {
            details.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(about, Html.FROM_HTML_MODE_LEGACY)?.trim()?.toString()
            else Html.fromHtml(about)?.trim()?.toString()
        }
    }
}