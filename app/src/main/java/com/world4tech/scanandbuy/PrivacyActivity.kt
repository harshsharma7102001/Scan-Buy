package com.world4tech.scanandbuy

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.world4tech.scanandbuy.databinding.ActivityPrivacyBinding

class PrivacyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val webView:WebView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        supportActionBar!!.setTitle("Privacy Policy")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        webView.apply {
            loadUrl( "https://www.app-privacy-policy.com/live.php?token=DAMZxYmQ1ga1u5ps5m2aDHfi6PPCiBmf")
            settings.safeBrowsingEnabled = true
            settings.javaScriptEnabled = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onBackPressed() {
        if (binding.webView.canGoBack()){
            binding.webView.goBack()
        }else{
            super.onBackPressed()
        }
    }
}