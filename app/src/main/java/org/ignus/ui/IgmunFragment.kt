package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_igmun.*
import org.ignus.R
import org.ignus.utils.openURL


class IgmunFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_igmun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
    }

    private fun setUpWebView() {

        val url = "https://ignus.org/events/mun/"
        val wb = igmunWebView
        wb.settings.javaScriptEnabled = true
        wb.settings.useWideViewPort = true
        wb.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        wb.loadUrl(url)

        wb.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (this@IgmunFragment.isVisible) igmunProgressBar.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                openURL(request?.url?.toString())
                return true
            }
        }
    }


}
