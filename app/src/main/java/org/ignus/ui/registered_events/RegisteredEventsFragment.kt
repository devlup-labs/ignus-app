package org.ignus.ui.registered_events


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_registered_events.*
import org.ignus.R
import org.ignus.db.viewmodels.RegisteredEventsViewModel


class RegisteredEventsFragment : Fragment() {

    private val viewModel: RegisteredEventsViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(RegisteredEventsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registered_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.registered_events_host_fragment)
        registered_events_bottom_nav_view.setupWithNavController(navController)

        setUpSwipeLayout()
        viewModel.refreshRegisteredEvents()
    }

    private fun setUpSwipeLayout() {
        registeredEventsSwipeLayout.setOnRefreshListener {
            viewModel.refreshRegisteredEvents()
        }

        registeredEventsSwipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        viewModel.loading.observe(this, Observer {
            if (this.isVisible) registeredEventsSwipeLayout.isRefreshing = it
        })
    }

}
