package com.efendi.salttest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import com.efendi.salttest.R
import com.google.android.material.snackbar.Snackbar

class DetailNewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        val webView: WebView = findViewById(R.id.webView)
        val parentView: LinearLayout = findViewById(R.id.parentView)
        val url = intent.getStringExtra("URL")

        if (url != null) {
            webView.loadUrl(url)
        } else {
            Snackbar.make(parentView,"No data to display", Snackbar.LENGTH_LONG).show()
        }

        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener(){
            onBackPressed()
        }

    }


}