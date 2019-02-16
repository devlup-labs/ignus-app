package org.ignus.ui.registered_events


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_registered_team.*
import org.ignus.R
import org.ignus.db.models.TeamEvents
import org.ignus.db.viewmodels.RegisteredEventsViewModel
import org.ignus.ui.adapter.RegisteredTeamEventListAdapter

class TeamFragment : Fragment() {

    private val viewModel: RegisteredEventsViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(RegisteredEventsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registered_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.teamEvents.observe(this, Observer {
            setUpRecyclerView(it)
        })
    }

    private fun setUpRecyclerView(events: List<TeamEvents>) {
        val recyclerView = registeredEventsTeamRecyclerView
        val adapter = RegisteredTeamEventListAdapter(requireActivity(), events, this)
        recyclerView.adapter = adapter

        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager
    }

}
