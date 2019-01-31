package org.ignus.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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


class MainActivity : AppCompatActivity() {

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.culturalEventsFragment,
                R.id.prakritiEventsFragment,
                R.id.workshopsFragment,
                R.id.contactsFragment,
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

        Glide.with(avatar)
            .load(R.drawable.placeholder)
            .apply(
                RequestOptions
                    .circleCropTransform()
                    .error(R.drawable.placeholder)
                    .placeholder(ColorDrawable(Color.BLACK))
            )
            .into(avatar)

        Glide.with(bgImg)
            .load("")
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