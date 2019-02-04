package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_about_ignus.*
import org.ignus.R


class AboutIgnusFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_ignus, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
    }

    private fun setUpWebView() {

        val wb = aboutWebView
        wb.settings.javaScriptEnabled = true
        wb.settings.loadWithOverviewMode = true
        wb.settings.useWideViewPort = true
        wb.loadUrl("https://ignus.org/#about_us")

        wb.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (this@AboutIgnusFragment.isVisible) aboutProgressBar.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl("https://ignus.org/#about_us")
                return true
            }
        }
    }

}
