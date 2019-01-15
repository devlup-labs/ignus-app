package org.ignus.app.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_event_categories.*
import org.ignus.app.R
import org.ignus.app.databinding.FragmentEventCategoriesBinding
import org.ignus.app.db.viewmodels.EventCategoriesViewModel
import org.ignus.app.di.component.DaggerFragmentComponent
import org.ignus.app.ui.MainActivity

class EventCategoriesFragment : Fragment() {
    private val viewModel: EventCategoriesViewModel by lazy {
        ViewModelProviders.of(this).get(EventCategoriesViewModel::class.java)
    }
    private lateinit var model: EventCategoriesFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        model = activity?.run {
            ViewModelProviders.of(this).get(EventCategoriesFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun injectDependencies() {
        val fragmentComponent = DaggerFragmentComponent.builder().build()
        fragmentComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentEventCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_categories, container, false)
        viewModel.eventCategories!!.observe(this, Observer {

        })
        return binding.root
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