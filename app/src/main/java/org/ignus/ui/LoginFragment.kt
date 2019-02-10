package org.ignus.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_login.*
import org.ignus.R
import org.ignus.db.viewmodels.LoginVM


class LoginFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginVM::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        consumeProfile()

        loginButton.setOnClickListener {
            val view2 = activity?.currentFocus
            view2.apply {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(view2?.windowToken, 0)
            }
            val igNumber = igNumberEditText.text.toString()
            val password = passwordEditText.text.toString()
            refreshUserProfile(igNumber, password)
        }
    }

    private fun initViews() {

        var pLength = 0

        igNumberEditText.addTextChangedListener {
            val text = it.toString()
            val length = it.toString().length

            if (pLength > length) {
                // Do nothing
            } else if (text.endsWith(" ")) {
                igNumberEditText.setText(StringBuilder(text).deleteCharAt(length - 1))
                igNumberEditText.setSelection(igNumberEditText.text.toString().length)
            } else if (length == 2 || length == 6) {
                igNumberEditText.setText(StringBuilder(text).append("-").toString())
                igNumberEditText.setSelection(igNumberEditText.text.toString().length)
            } else if (length == 10) {
                igNumberEditText.clearFocus()
                passwordEditText.requestFocus()
                passwordEditText.isCursorVisible = true
            }
            pLength = igNumberEditText.text.toString().length

            loginButton.isEnabled = !(it.isNullOrBlank() || passwordEditText.text.isNullOrBlank())
        }

        passwordEditText.addTextChangedListener {
            loginButton.isEnabled = !(it.isNullOrBlank() || igNumberEditText.text.isNullOrBlank())
        }
    }

    private fun refreshUserProfile(username: String, password: String) {
        viewModel.refreshUserProfile(username, password)
    }

    private fun refreshUserProfile() {
        viewModel.refreshUserProfile()
    }

    private fun consumeProfile() {
        viewModel.userProfile.observe(this, Observer {
            Log.d("suthar", "User Profile: $it")
        })

        viewModel.getLoading().observe(this, Observer {
            if (this@LoginFragment.isVisible) {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        })

        viewModel.getSuccess().observe(this, Observer {
            if (it) {
                Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                this@LoginFragment.fragmentManager?.popBackStack()
            }
        })
    }

}
