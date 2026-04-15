package com.example.learningportalapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var etUrl: EditText
    private lateinit var progressBar: ProgressBar

    private val HOME_URL = "https://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        webView = findViewById(R.id.webView)
        etUrl = findViewById(R.id.etUrl)
        progressBar = findViewById(R.id.progressBar)

        setupWebView()
        setupButtons()
        setupHardwareBackButton()

        loadUrlInWebView(HOME_URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE

                if (url != "file:///android_asset/offline.html") {
                    etUrl.setText(url)
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                webView.loadUrl("file:///android_asset/offline.html")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
            }
        }
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            if (webView.canGoBack()) webView.goBack() else Toast.makeText(this, "No more history", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnForward).setOnClickListener {
            if (webView.canGoForward()) webView.goForward()
        }

        findViewById<ImageButton>(R.id.btnRefresh).setOnClickListener { webView.reload() }

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener { loadUrlInWebView(HOME_URL) }

        findViewById<Button>(R.id.btnGo).setOnClickListener { loadUrlFromAddressBar() }

        etUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                loadUrlFromAddressBar()
                true
            } else {
                false
            }
        }

        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            loadUrlInWebView("https://www.google.com")
        }
        findViewById<Button>(R.id.btnYoutube).setOnClickListener {
            loadUrlInWebView("https://www.youtube.com")
        }
        findViewById<Button>(R.id.btnWiki).setOnClickListener {
            loadUrlInWebView("https://www.wikipedia.org")
        }
        findViewById<Button>(R.id.btnKhan).setOnClickListener {
            loadUrlInWebView("https://www.khanacademy.org")
        }
        findViewById<Button>(R.id.btnUni).setOnClickListener {
            loadUrlInWebView("https://www.aiub.edu")
        }
    }

    private fun loadUrlFromAddressBar() {
        var urlInput = etUrl.text.toString().trim()
        if (urlInput.isNotEmpty()) {
            if (!urlInput.startsWith("http://") && !urlInput.startsWith("https://")) {
                urlInput = "https://$urlInput"
            }
            loadUrlInWebView(urlInput)
        }
    }

    private fun loadUrlInWebView(url: String) {
        if (isNetworkAvailable()) {
            webView.loadUrl(url)
        } else {
            webView.loadUrl("file:///android_asset/offline.html")
        }
    }

    private fun setupHardwareBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}