package org.ignus.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_event_categories.*
import org.ignus.R
import org.ignus.db.viewmodels.EventCategoriesViewModel


class EventCategoriesFragment : Fragment() {

    private val viewModel: EventCategoriesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(EventCategoriesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshEventCategories()
        Log.d("suthar", "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("suthar", "onViewCreated")

        val navController = Navigation.findNavController(requireActivity(), R.id.bottom_nav_host_fragment)
        bottom_nav_view.setupWithNavController(navController)

        setUpSwipeLayout()
    }

    private fun setUpSwipeLayout() {
        eventCategoriesSwipeLayout.setOnRefreshListener {
            viewModel.refreshEventCategories()
        }

        eventCategoriesSwipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        viewModel.getLoading().observe(this, Observer {
            if (this.isVisible) eventCategoriesSwipeLayout.isRefreshing = it
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("suthar", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("suthar", "onPause")
    }

    override fun onDestroy() {
        Log.d("suthar", "onDestroy")
        super.onDestroy()
        viewModel.disposeElements()
    }

}
