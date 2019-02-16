package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_informal_events.*
import org.ignus.R
import org.ignus.db.viewmodels.EventCategoriesViewModel
import org.ignus.ui.adapter.EventCategoriesAdapter
import org.ignus.ui.itemdecoration.EventCategoryItemDecoration


class InformalEventsFragment : Fragment() {

    private val adapter: EventCategoriesAdapter by lazy { EventCategoriesAdapter(requireActivity()) }

    private val viewModel: EventCategoriesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(EventCategoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_informal_events, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshEventCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.eventCategories.observe(this, Observer { list ->
            val data = list.filter { it.parent_type == "5" }
            adapter.setData(data)
        })

        setUpSwipeLayout()
    }

    private fun setUpSwipeLayout() {
        informalSwipeLayout.setOnRefreshListener {
            viewModel.refreshEventCategories()
        }

        informalSwipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        viewModel.getLoading().observe(this, Observer {
            if (this.isVisible) informalSwipeLayout.isRefreshing = it
        })
    }

    private fun setUpRecyclerView() {
        informalEventsRecyclerView.adapter = adapter
        informalEventsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        informalEventsRecyclerView.addItemDecoration(EventCategoryItemDecoration())
    }

}
