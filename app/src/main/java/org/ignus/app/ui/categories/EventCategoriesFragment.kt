package org.ignus.app.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.ignus.app.R
import org.ignus.app.databinding.FragmentEventCategoriesBinding
import org.ignus.app.db.viewmodels.EventCategoriesViewModel
import org.ignus.app.di.component.DaggerFragmentComponent

class EventCategoriesFragment : Fragment() {
    private val viewModel: EventCategoriesViewModel by lazy {
        ViewModelProviders.of(this).get(EventCategoriesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentEventCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_categories, container, false)
        viewModel.eventCategories!!.observe(this, Observer {

        })
        return binding.root
    }

    private fun injectDependencies() {
        val fragmentComponent = DaggerFragmentComponent.builder().build()
        fragmentComponent.inject(this)
    }
}