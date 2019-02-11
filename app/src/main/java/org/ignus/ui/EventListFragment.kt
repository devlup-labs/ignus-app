package org.ignus.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_event_list.*
import org.ignus.db.models.EventCategory
import org.ignus.ui.adapter.EventListAdapter


class EventListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(org.ignus.R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getSerializable("event-category") as EventCategory
        Log.d("suthar", "Event Category: ${category.events?.size}")
        (activity as MainActivity).setTitle(category.name)

        setUpRecyclerView(category)
    }

    private fun setUpRecyclerView(category: EventCategory) {
        val adapter = EventListAdapter(requireActivity(), category, this)
        eventListRecyclerView.adapter = adapter

        val layoutManager = GridLayoutManager(context, 1)
        eventListRecyclerView.layoutManager = layoutManager

        // val dividerItemDecoration = DividerItemDecoration(eventListRecyclerView.context, layoutManager.orientation)
        // eventListRecyclerView.addItemDecoration(dividerItemDecoration)
    }

}
