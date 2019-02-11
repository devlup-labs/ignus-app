package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_workshops.*
import org.ignus.R
import org.ignus.db.viewmodels.WorkshopListViewModel
import org.ignus.ui.adapter.WorkshopListAdapter


class WorkshopsFragment : Fragment() {

    private val adapter by lazy { WorkshopListAdapter(requireActivity()) }

    private val viewModel: WorkshopListViewModel by lazy {
        ViewModelProviders.of(this).get(WorkshopListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workshops, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refreshWorkshopList()

        viewModel.workshopList.observe(this, Observer {
            adapter.setList(it)
        })

        setUpSwipeLayout()
        setUpRecyclerView()
    }

    private fun setUpSwipeLayout() {
        workshopSwipeLayout.setOnRefreshListener {
            viewModel.refreshWorkshopList()
        }

        workshopSwipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        viewModel.getLoading().observe(this, Observer {
            if (this.isVisible) workshopSwipeLayout.isRefreshing = it
        })
    }

    private fun setUpRecyclerView() {
        workshopRecyclerView.adapter = adapter
        workshopRecyclerView.setHasFixedSize(true)

        val layoutManager = GridLayoutManager(context, 1)
        workshopRecyclerView.layoutManager = layoutManager

        //val dividerItemDecoration = DividerItemDecoration(workshopRecyclerView.context, layoutManager.orientation)
        //workshopRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disposeElements()
    }
}
