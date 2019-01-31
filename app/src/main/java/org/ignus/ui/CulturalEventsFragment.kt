package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_cultural_events.*
import org.ignus.R
import org.ignus.db.viewmodels.EventCategoriesViewModel
import org.ignus.ui.adapter.EventCategoriesAdapter
import org.ignus.ui.itemdecoration.EventCategoryItemDecoration


class CulturalEventsFragment : Fragment() {

    private val adapter: EventCategoriesAdapter by lazy { EventCategoriesAdapter(requireActivity()) }

    private val viewModel: EventCategoriesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(EventCategoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cultural_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setTitle("Cultural")

        setUpRecyclerView()

        viewModel.eventCategories.observe(this, Observer { list ->
            val data = list.filter { it.parent_type == "1" }
            adapter.setData(data)
        })

    }

    private fun setUpRecyclerView() {
        culturalEventsRecyclerView.adapter = adapter
        culturalEventsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        culturalEventsRecyclerView.addItemDecoration(EventCategoryItemDecoration())
        // culturalEventsRecyclerView.addItemDecoration(WorkshopListItemDecoration())
    }

}
