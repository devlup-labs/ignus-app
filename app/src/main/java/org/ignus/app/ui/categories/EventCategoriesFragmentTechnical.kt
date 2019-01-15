package org.ignus.app.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.ignus.app.R
import org.ignus.app.db.models.EventCategory

class EventCategoriesFragmentTechnical : Fragment() {

    private lateinit var model: EventCategoriesFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(EventCategoriesFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_categories_fragment_technical, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.eventCategories?.observe(this, Observer {
            val list = it.filter { eventCategory -> eventCategory.parentType == "1" }
            updateUi(list)
        })
    }

    private fun updateUi(list: List<EventCategory>) {
        val adapter = EventCategoriesAdapter(list)
    }
}
