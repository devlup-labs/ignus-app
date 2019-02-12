package org.ignus.ui


import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_login.*
import org.ignus.App
import org.ignus.R
import org.ignus.config.LOGIN_PAGE_BG_IMAGE
import org.ignus.db.viewmodels.LoginViewModel


class LoginFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sp by lazy { PreferenceManager.getDefaultSharedPreferences(App.instance) }
        if (sp.getString("jwt-token", null) != null) {
            Toast.makeText(activity, "User already logged In", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

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

        Glide.with(topImage)
            .load(LOGIN_PAGE_BG_IMAGE)
            .apply(
                RequestOptions()
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
            )
            .into(topImage)
    }

    private fun refreshUserProfile(username: String, password: String) {
        viewModel.refreshUserProfile(username, password)
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
                findNavController().popBackStack()
            }
        })
    }

}
