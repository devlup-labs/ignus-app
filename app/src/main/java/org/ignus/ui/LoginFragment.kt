package org.ignus.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.ignus.R
import org.ignus.db.viewmodels.LoginVM

class LoginFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginVM::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshUserProfile("IG-ABH-737", "password-password")
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
