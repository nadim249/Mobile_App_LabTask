package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBrowse: Button = findViewById(R.id.btnBrowse)
        val btnMyBookings: Button = findViewById(R.id.btnMyBookings)
        val btnNotifications: Button = findViewById(R.id.btnNotifications)
        val btnProfile: Button = findViewById(R.id.btnProfile)
        val btnRegisterFeatured: Button = findViewById(R.id.btnRegisterFeatured)

        btnBrowse.setOnClickListener {
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
        }

        btnRegisterFeatured.setOnClickListener {
            val featuredEvent = Event(
                id = "1",
                title = "Tech Symposium 2026",
                date = "Oct 12, 2026",
                time = "10:00 AM",
                venue = "Main Auditorium",
                category = "Tech",
                description = "A grand gathering of tech enthusiasts featuring keynote speakers from top tech giants, hands-on workshops, and networking opportunities.",
                price = 15.0,
                totalSeats = 48,
                availableSeats = 30,
                imageRes = R.drawable.img
            )

            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("EVENT_DATA", featuredEvent)
            startActivity(intent)
        }

        btnMyBookings.setOnClickListener {
            Toast.makeText(this, "My Bookings feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        btnNotifications.setOnClickListener {
            Toast.makeText(this, "You have 0 new notifications.", Toast.LENGTH_SHORT).show()
        }

        btnProfile.setOnClickListener {
            Toast.makeText(this, "Opening Profile...", Toast.LENGTH_SHORT).show()
        }

        btnMyBookings.setOnClickListener {
            val intent = Intent(this, MyBookingsActivity::class.java)
            startActivity(intent)
        }
    }
}