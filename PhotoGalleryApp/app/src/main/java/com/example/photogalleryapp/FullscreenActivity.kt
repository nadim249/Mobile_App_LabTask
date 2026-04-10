package com.example.photogalleryapp

import android.graphics.Matrix
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class FullscreenActivity : AppCompatActivity() {
    private lateinit var matrix: Matrix
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable Edge-to-Edge
        enableEdgeToEdge()
        
        setContentView(R.layout.activity_fullscreen)

        // Hide System Bars for a true immersive fullscreen experience
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        val imgResId = intent.getIntExtra("IMG_RES", 0)
        val ivFullscreen = findViewById<ImageView>(R.id.ivFullscreen)
        ivFullscreen.setImageResource(imgResId)

        // Back button logic
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        matrix = Matrix()
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 5.0f))
                matrix.setScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
                ivFullscreen.imageMatrix = matrix
                return true
            }
        })
    }

    override fun onTouchEvent(event: android.view.MotionEvent?): Boolean {
        event?.let { scaleGestureDetector.onTouchEvent(it) }
        return true
    }
}
