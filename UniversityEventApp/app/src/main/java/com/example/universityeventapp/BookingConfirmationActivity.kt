package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Confirmation"

        val tvConfirmTitle: TextView = findViewById(R.id.tvConfirmTitle)
        val tvConfirmDetails: TextView = findViewById(R.id.tvConfirmDetails)
        val ivQRCode: ImageView = findViewById(R.id.ivQRCode)
        val btnBackHome: Button = findViewById(R.id.btnBackHome)

        val event = intent.getSerializableExtra("EVENT_DATA") as? Event
        val seatCount = intent.getIntExtra("SEAT_COUNT", 0)

        event?.let {
            tvConfirmTitle.text = "Booking Confirmed!"
            tvConfirmDetails.text = "Event: ${it.title}\nSeats Booked: $seatCount\nVenue: ${it.venue}"
            
            BookingManager.addBooking(it)
        }

        ivQRCode.setImageResource(R.drawable.img_1)

        btnBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}