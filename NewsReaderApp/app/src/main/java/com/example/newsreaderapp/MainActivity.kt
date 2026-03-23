package com.example.newsreaderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var isBookmarked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val scrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        val fabTop = findViewById<FloatingActionButton>(R.id.fabBackToTop)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)


        val secIntro = findViewById<TextView>(R.id.secIntro)
        val secKey = findViewById<TextView>(R.id.secKey)
        val secAnalysis = findViewById<TextView>(R.id.secAnalysis)
        val secConclusion = findViewById<TextView>(R.id.secConclusion)

        // 1. Quick Navigation Logic
        findViewById<Button>(R.id.btnNavIntro).setOnClickListener { scrollToView(scrollView, secIntro) }
        findViewById<Button>(R.id.btnNavKey).setOnClickListener { scrollToView(scrollView, secKey) }
        findViewById<Button>(R.id.btnNavAnalysis).setOnClickListener { scrollToView(scrollView, secAnalysis) }
        findViewById<Button>(R.id.btnNavConclusion).setOnClickListener { scrollToView(scrollView, secConclusion) }

        // 2. Back to Top
        fabTop.setOnClickListener {
            scrollView.smoothScrollTo(0, 0)
        }

        // 3. Bookmark Toggle
        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            if (isBookmarked) {
                btnBookmark.setImageResource(android.R.drawable.btn_star_big_on)
                Toast.makeText(this, "Article Bookmarked", Toast.LENGTH_SHORT).show()
            } else {
                btnBookmark.setImageResource(android.R.drawable.btn_star_big_off)
                Toast.makeText(this, "Bookmark Removed", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Share Logic
        btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Read this article: ${tvTitle.text}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun scrollToView(scrollView: NestedScrollView, view: TextView) {
        scrollView.smoothScrollTo(0, view.top)
    }
}