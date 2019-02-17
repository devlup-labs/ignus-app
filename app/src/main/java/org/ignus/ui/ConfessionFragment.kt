package org.ignus.ui


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_confession.*
import org.ignus.App
import org.ignus.R
import org.ignus.db.models.Message
import org.ignus.db.viewmodels.ConfessionViewModel
import org.ignus.db.viewmodels.LoginViewModel
import org.ignus.ui.adapter.ConfessionAdapter
import org.ignus.ui.itemdecoration.EventCategoryItemDecoration


class ConfessionFragment : Fragment() {

    private val adapter by lazy { ConfessionAdapter(model) }
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
        var username: String? = null
        userModel.userProfile.observe(this, Observer {
            username = it.user?.username
            adapter.setUsername(username ?: "")
        })

        editText.addTextChangedListener {
            if (it?.isBlank() != true) send.setColorFilter(Color.parseColor("#009688"))
            else send.setColorFilter(Color.GRAY)
        }

        send.setOnClickListener {
            if (editText.text.isNullOrBlank()) return@setOnClickListener
            if (!loggedIn(it)) return@setOnClickListener
            val text = editText.text.toString().trim()
            val timestamp = System.currentTimeMillis()
            val message = Message(text, username, timestamp, null)
            model.sendNewMessage(message)
            editText.setText("")
        }
    }

    private fun loggedIn(view: View): Boolean {
        val sp by lazy { PreferenceManager.getDefaultSharedPreferences(App.instance) }
        if (sp.getString("jwt-token", null) == null) {
            hideSoftKeyboard()
            val snackbar = Snackbar.make(
                view, "To prevent spam you can't send message without login. We do not store your personal " +
                        "information. For more info read out Privacy and Policy.",
                Snackbar.LENGTH_LONG
            )//.setAction("Login") { findNavController().navigate(R.id.loginFragment) }
            snackbar.duration = 6000
            val snackTextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            snackTextView.maxLines = 4
            snackbar.show()
            return false
        }
        return true
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

    private fun hideSoftKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }
}
