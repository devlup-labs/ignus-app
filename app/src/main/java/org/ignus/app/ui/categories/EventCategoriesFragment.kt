package org.ignus.app.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_event_categories.*
import org.ignus.app.R
import org.ignus.app.ui.MainActivity

class EventCategoriesFragment : Fragment() {

    private lateinit var model: EventCategoriesFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(EventCategoriesFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (this.activity as MainActivity).setToolbarTitle("Event Categories")
        val navController = Navigation.findNavController(requireActivity(), R.id.bottom_nav_host_fragment)
        bottom_navigation_view.setupWithNavController(navController)

        getData()
    }

    private fun getData() {
        // Update data in view Model
        model.init()
    }

}