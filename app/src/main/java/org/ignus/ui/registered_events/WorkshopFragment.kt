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
import kotlinx.android.synthetic.main.fragment_registered_workshop.*
import org.ignus.R
import org.ignus.db.viewmodels.RegisteredEventsViewModel
import org.ignus.ui.adapter.WorkshopListAdapter

class WorkshopFragment : Fragment() {

    private val adapter by lazy { WorkshopListAdapter(requireActivity()) }

    private val viewModel: RegisteredEventsViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(RegisteredEventsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registered_workshop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.workshops.observe(this, Observer {
            adapter.setList(it)
        })
    }

    private fun setUpRecyclerView() {

        val recyclerView = registeredEventsWorkshopRecyclerView

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

}
