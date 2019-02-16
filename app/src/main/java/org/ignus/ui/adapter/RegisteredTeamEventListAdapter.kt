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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.ignus.R
import org.ignus.db.models.Event
import org.ignus.db.models.TeamEvents
import org.ignus.db.viewmodels.EventDetailsViewModel
import org.ignus.utils.formatDate
import org.ignus.utils.openGoogleMaps

class RegisteredTeamEventListAdapter(
    private val activity: Activity,
    private val events: List<TeamEvents>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }

    private val viewModel: EventDetailsViewModel by lazy {
        ViewModelProviders.of(fragment).get(EventDetailsViewModel::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (events.isNotEmpty()) MyViewHolder1(
            LayoutInflater.from(parent.context).inflate(R.layout.event_list_card_1, parent, false)
        )
        else MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.no_adapter_item, parent, false))

    }

    override fun getItemCount() = events.size

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (events.isNotEmpty()) (holder as MyViewHolder1).bindData(events[position])
    }


    inner class MyViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        private val parent: ConstraintLayout = view.findViewById(R.id.parentLayout)
        private val title: TextView = view.findViewById(R.id.title)
        private val teamDetails: TextView = view.findViewById(R.id.team_details)
        private val locationLayout: ConstraintLayout = view.findViewById(R.id.location_layout)
        private val location: TextView = view.findViewById(R.id.location)
        private val time: TextView = view.findViewById(R.id.time)

        fun bindData(teamEvent: TeamEvents) {
            val event = teamEvent.event
            title.text = event.name
            teamDetails.text = activity.getString(R.string.team_size, event.min_team_size, event.max_team_size)
            location.text = event.location?.name
            time.text = event.start_time?.formatDate

            parent.setOnClickListener { showDetails(teamEvent) }
            locationLayout.setOnClickListener { openGoogleMaps(event.location) }

        }

        @SuppressLint("InflateParams")
        private fun showDetails(teamEvent: TeamEvents) {

            val event: Event = teamEvent.event

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
            viewModel.eventDetails.observe(fragment, Observer {

                var members = ""

                for (member in teamEvent.members) {
                    members += activity.getString(
                        R.string.member_name,
                        member.user.username,
                        member.user.first_name,
                        member.user.last_name
                    )
                }

                val leader = activity.getString(
                    R.string.leader_name,
                    teamEvent.leader.user.username,
                    teamEvent.leader.user.first_name,
                    teamEvent.leader.user.last_name
                )

                val detailsTxt =
                    activity.getString(R.string.registered_event_details, leader, members, it.about, it.details)

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

            viewModel.eventDetailsError.observe(fragment, Observer {
                Toast.makeText(activity, "Something wrong happened!", Toast.LENGTH_LONG).show()
            })

            viewModel.getLoading().observe(fragment, Observer {
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

            title.text = activity.getString(R.string.organisers)
            recyclerView.adapter = OrganiserListAdapter(event.organiser_list ?: return)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}