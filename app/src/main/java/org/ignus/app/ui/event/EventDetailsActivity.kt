package org.ignus.app.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_event_details.*
import org.ignus.app.R

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        setUpViewPager()
        setUpActionBar()
    }

    private fun setUpViewPager() {
        view_pager.adapter = SectionsPagerAdapter(supportFragmentManager)
        view_pager.offscreenPageLimit = 2
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun setUpActionBar(/*event :Event*/){
        setSupportActionBar(toolbar)
        //supportActionBar?.title = event.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Glide.with(this).load(event.cover_url).into(ed_logo)
    }

}

private class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    // getItem is called to instantiate the fragment for the given page.
    // Return a PlaceholderFragment (defined as a static inner class below).
    override fun getItem(position: Int): Fragment = PlaceholderFragment.newInstance(position + 1)

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "About"
            1 -> "Details"
            2 -> "Organisers"
            else -> "Error"
        }
    }
}

class PlaceholderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER)
        return when (sectionNumber) {
            1 -> inflater.inflate(R.layout.dummy, container, false)
            2 -> inflater.inflate(R.layout.dummy, container, false)
            3 -> inflater.inflate(R.layout.dummy, container, false)
            else -> null
        }
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}
