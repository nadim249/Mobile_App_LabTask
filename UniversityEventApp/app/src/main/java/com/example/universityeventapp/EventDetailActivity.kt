package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


class EventDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Event Details"

        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvInfo: TextView = findViewById(R.id.tvDetailInfo)
        val tvDesc: TextView = findViewById(R.id.tvDetailDesc)
        val btnBookSeats: Button = findViewById(R.id.btnBookSeats)
        val ivBanner: ImageView = findViewById(R.id.ivDetailBanner)
        val tvTimer: TextView = findViewById(R.id.tvTimer)

        // Retrieve data passed from EventsActivity
        val event = intent.getSerializableExtra("EVENT_DATA") as? Event

        event?.let {
            tvTitle.text = it.title
            tvInfo.text = "${it.date} • ${it.time}\n${it.venue}"
            tvDesc.text = it.description
            ivBanner.setImageResource(it.imageRes)
            ivBanner.transitionName = "event_image_transition"
            
            startCountdown(it.date, tvTimer)
        }

        btnBookSeats.setOnClickListener {
            val intent = Intent(this, SeatBookingActivity::class.java)
            intent.putExtra("EVENT_DATA", event)
            startActivity(intent)
        }
    }

    private fun startCountdown(dateString: String, tvTimer: TextView) {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        try {
            val eventDate = sdf.parse(dateString)
            val currentTime = System.currentTimeMillis()
            val diff = eventDate?.let { it.time - currentTime } ?: 0L

            if (diff > 0) {
                object : CountDownTimer(diff, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                        tvTimer.text = "Starts in: ${days}d ${hours}h ${minutes}m ${seconds}s"
                    }
                    override fun onFinish() {
                        tvTimer.text = "Event Started!"
                    }
                }.start()
            } else {
                tvTimer.text = "Event Passed"
            }
        } catch (e: Exception) {
            tvTimer.text = ""
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}