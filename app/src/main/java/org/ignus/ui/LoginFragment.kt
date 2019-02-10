package org.ignus.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


        igNumberEditText.addTextChangedListener {
            val text = it.toString()
            val length = it.toString().length

            if (text.endsWith(" ")) {

            }

            if (length == 2 || length == 6) {
                igNumberEditText.setText(StringBuilder(text).insert(text.length, "-").toString())
                igNumberEditText.setSelection(igNumberEditText.text.toString().length)
            }

            loginButton.isEnabled = !(it.isNullOrBlank() || passwordEditText.text.isNullOrBlank())
        }

        passwordEditText.addTextChangedListener {
            loginButton.isEnabled = !(it.isNullOrBlank() || igNumberEditText.text.isNullOrBlank())
        }

        loginButton.setOnClickListener {
            val igNumber = igNumberEditText.text.toString()
            val password = passwordEditText.text.toString()

            refreshUserProfile(igNumber, password)
        }

        // refreshUserProfile("IG-ABH-737", "password-password")
        consumeProfile()
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
    }

}
