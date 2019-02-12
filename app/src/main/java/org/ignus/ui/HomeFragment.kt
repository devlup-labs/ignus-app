package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.ignus.R
import org.ignus.config.HOME_PAGE_URL
import java.sql.Date
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDaysLeft()
        setUpWebView()
        setUpSwipeLayout()
    }

    private fun setDaysLeft() {
        val date = Date(1550687400000)
        val diff = date.time - System.currentTimeMillis()
        daysLeft.text = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString()
    }

    private fun setUpWebView() {

        val wb = homeWebView
        wb.settings.javaScriptEnabled = true
        wb.settings.loadWithOverviewMode = true
        wb.settings.useWideViewPort = true
        wb.loadUrl(HOME_PAGE_URL)

        wb.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (this@HomeFragment.isVisible) {
                    wb.visibility = View.VISIBLE
                    homeSwipeLayout.isRefreshing = false
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return true
            }
        }
    }

    private fun setUpSwipeLayout() {

        homeSwipeLayout.setOnRefreshListener {
            homeWebView.loadUrl(HOME_PAGE_URL)
        }

        homeSwipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        homeSwipeLayout.viewTreeObserver.addOnScrollChangedListener {
            if (homeWebView != null) homeSwipeLayout.isEnabled = homeWebView.scrollY == 0
        }
    }
}
