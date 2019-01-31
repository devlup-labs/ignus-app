package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_prakriti_events.*
import org.ignus.R
import org.ignus.db.viewmodels.EventCategoriesViewModel
import org.ignus.ui.adapter.EventCategoriesAdapter
import org.ignus.ui.itemdecoration.EventCategoryItemDecoration


class PrakritiEventsFragment : Fragment() {

    private val adapter: EventCategoriesAdapter by lazy { EventCategoriesAdapter(requireActivity()) }

    private val viewModel: EventCategoriesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(EventCategoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prakriti_events, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshEventCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.eventCategories.observe(this, Observer { list ->
            val data = list.filter { it.parent_type == "4" }
            adapter.setData(data)
        })

        setUpSwipeLayout()
    }

    private fun setUpSwipeLayout() {
        prakritiSwipeLayout.setOnRefreshListener {
            viewModel.refreshEventCategories()
        }

        prakritiSwipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        viewModel.getLoading().observe(this, Observer {
            if (this.isVisible) prakritiSwipeLayout.isRefreshing = it
        })
    }

    private fun setUpRecyclerView() {
        prakritiEventsRecyclerView.adapter = adapter
        prakritiEventsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        prakritiEventsRecyclerView.addItemDecoration(EventCategoryItemDecoration())
    }


}
