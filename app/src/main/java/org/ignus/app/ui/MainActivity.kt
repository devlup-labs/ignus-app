package org.ignus.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import org.ignus.app.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(findNavController(this, R.id.nav_host_fragment), drawerLayout)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    private fun setupNavigation() {
        val navController = findNavController(this, R.id.nav_host_fragment)

        // Update action bar to reflect navigation
        setupActionBarWithNavController(this, navController, drawerLayout)

        // Handle nav drawer item clicks
//        navigationView.setNavigationItemSelectedListener { menuItem ->
//            Log.d("suthar", "Nav Clicked, ${menuItem.itemId}")
//            menuItem.isChecked = true
//            drawerLayout.closeDrawers()
//            true
//        }

        // Tie nav graph to items in nav drawer
        setupWithNavController(navigationView, navController)
    }
}
