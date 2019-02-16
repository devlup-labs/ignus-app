package org.ignus.ui


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_confession.*
import org.ignus.R
import org.ignus.db.models.Message
import org.ignus.db.viewmodels.ConfessionViewModel
import org.ignus.db.viewmodels.LoginViewModel
import org.ignus.ui.adapter.ConfessionAdapter
import org.ignus.ui.itemdecoration.EventCategoryItemDecoration


class ConfessionFragment : Fragment() {

    private val adapter by lazy { ConfessionAdapter() }
    private val model by lazy { ViewModelProviders.of(this).get(ConfessionViewModel::class.java) }
    private val userModel by lazy { ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confession, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.messages.observe(this, Observer {
            adapter.setData(it)

            val x = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            if (x + 5 > it.size) recyclerView.scrollToPosition(it.size - 1)
        })

        setUp()
        setUpRecyclerView()
    }

    private fun setUp() {
        var igNumber: String? = null
        userModel.userProfile.observe(this, Observer {
            igNumber = it.user?.username
        })

        editText.addTextChangedListener {
            if (it?.isBlank() != true) send.setColorFilter(Color.parseColor("#009688"))
            else send.setColorFilter(Color.GRAY)
        }

        send.setOnClickListener {
            if (editText.text.isNullOrBlank()) return@setOnClickListener
            val text = editText.text.toString().trim()
            val timestamp = System.currentTimeMillis()
            val message = Message(text, igNumber, timestamp)
            model.sendNewMessage(message)
            editText.setText("")
        }
    }

    private fun setUpRecyclerView() {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(EventCategoryItemDecoration(8))

        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.postDelayed({
                    val position = if (adapter.itemCount - 1 > -1) adapter.itemCount - 1 else 0

                    recyclerView.smoothScrollToPosition(position)
                }, 100)
            }
        }
    }

}
