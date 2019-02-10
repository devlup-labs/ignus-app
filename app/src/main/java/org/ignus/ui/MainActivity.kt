package org.ignus.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.ignus.R
import org.ignus.db.viewmodels.LoginVM
import org.ignus.utils.boyIcons
import org.ignus.utils.girlIcons
import org.ignus.utils.random


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
                R.id.aboutIgnusFragment
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

            val avatarIcon = if (it.gender?.toLowerCase() == "f") girlIcons.random()
            else boyIcons.random()

            Glide.with(avatar)
                .load(avatarIcon)
                .apply(
                    RequestOptions
                        .circleCropTransform()
                        .placeholder(ColorDrawable(Color.BLACK))
                )
                .into(avatar)


            title.text = getString(R.string.full_name, it.user?.first_name, it.user?.first_name)
            subTitle.text = it.user?.email

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

}
