package org.ignus.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.ignus.R
import org.ignus.db.models.UserProfile
import org.ignus.db.models.qrUrl
import org.ignus.db.viewmodels.LoginVM


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginVM::class.java) }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.culturalEventsFragment,
                R.id.prakritiEventsFragment,
                R.id.workshopsFragment,
                R.id.contactsFragment,
                R.id.mapsFragment,
                R.id.sponsorsFragment,
                R.id.confessionFragment
            ),
            drawerLayout
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setUpNavigation()
        setUpNavHeader()
    }

    private fun setUpNavigation() {
        val navController = findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
    }

    private fun setUpNavHeader() {

        val navHeader = navigationView.getHeaderView(0)

        val avatar = navHeader.nav_header_profile_img
        val bgImg: ImageView = navHeader.nav_header_bg_img

        val title = navHeader.nav_header_name
        val subTitle = navHeader.nav_header_email


        viewModel.refreshUserProfile()
        viewModel.userProfile.observe(this, Observer {

            // val avatarIcon = if (it.gender?.toLowerCase() == "f") girlIcons.random()
            // else boyIcons.random()
            val avatarIcon = it.qrUrl("256")

            Glide.with(avatar)
                .load(avatarIcon)
                .apply(
                    RequestOptions
                        .circleCropTransform()
                        .placeholder(ColorDrawable(Color.BLACK))
                )
                .into(avatar)


            title.text = getString(R.string.full_name, it.user?.first_name, it.user?.last_name)
            subTitle.text = it.user?.email

            avatar.setOnClickListener { _ ->
                showQR(it)
            }

        })

        Glide.with(bgImg)
            .load("https://drive.google.com/uc?export=download&id=11pfTm0kR5prmJn9CAG0ctthQ3sEL-gLf")
            .apply(
                RequestOptions()
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
            )
            .into(bgImg)
    }

    fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(item)

    override fun onSupportNavigateUp() = findNavController(this, R.id.nav_host_fragment).navigateUp(appBarConfiguration)

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_options_menu, menu)
        return true
    }

    private fun showQR(user: UserProfile) {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.qr_code_dialog, null)
        builder.setView(view)
        val dialog = builder.create()

        val title = view.findViewById<TextView>(R.id.title)
        val avatar = view.findViewById<ImageView>(R.id.image)
        val positiveBtn = view.findViewById<Button>(R.id.positive_btn)

        val avatarIcon = user.qrUrl("512")

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(avatar)
            .load(avatarIcon)
            .apply(
                RequestOptions()
                    .error(R.drawable.placeholder)
                    .placeholder(circularProgressDrawable)
            )
            .into(avatar)

        title.text = getString(R.string.full_name, user.user?.first_name, user.user?.last_name)

        positiveBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }
}
