package com.example.universityeventapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class SeatBookingActivity : AppCompatActivity() {

    private lateinit var seatGrid: GridLayout
    private lateinit var tvSummary: TextView
    private lateinit var btnConfirmBooking: Button

    private val totalSeats = 48
    private var selectedSeats = mutableSetOf<Int>()
    private var preBookedSeats = setOf<Int>()
    private var eventPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_booking)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Book Your Seats"

        seatGrid = findViewById(R.id.seatGrid)
        tvSummary = findViewById(R.id.tvSummary)
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking)

        val event = intent.getSerializableExtra("EVENT_DATA") as? Event
        eventPrice = event?.price ?: 15.0 // Fallback price

        setupSeats()

        btnConfirmBooking.setOnClickListener {
            if (selectedSeats.isNotEmpty()) {
                val intent = Intent(this, BookingConfirmationActivity::class.java)
                intent.putExtra("EVENT_DATA", event)
                intent.putExtra("SEAT_COUNT", selectedSeats.size)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select at least one seat.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSeats() {
        val numBooked = (totalSeats * 0.30).toInt()
        preBookedSeats = (0 until totalSeats).shuffled().take(numBooked).toSet()

        for (i in 0 until totalSeats) {
            val seatView = View(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 120
                    height = 120
                    setMargins(8, 8, 8, 8)
                }

                if (preBookedSeats.contains(i)) {
                    setBackgroundColor(Color.RED)
                    isEnabled = false
                } else {
                    setBackgroundColor(Color.GREEN)
                    setOnClickListener { handleSeatClick(this, i) }
                }
            }
            seatGrid.addView(seatView)
        }
    }

    private fun handleSeatClick(view: View, seatIndex: Int) {
        if (selectedSeats.contains(seatIndex)) {
            // Deselect
            selectedSeats.remove(seatIndex)
            view.setBackgroundColor(Color.GREEN)
        } else {
            // Select
            selectedSeats.add(seatIndex)
            view.setBackgroundColor(Color.BLUE)
        }
        updateSummary()
    }

    private fun updateSummary() {
        val totalCost = selectedSeats.size * eventPrice
        tvSummary.text = "${selectedSeats.size} seats selected | Total: $${String.format("%.2f", totalCost)}"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (selectedSeats.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Discard Booking?")
                .setMessage("You have selected seats. Are you sure you want to leave?")
                .setPositiveButton("Yes") { _, _ -> finish() }
                .setNegativeButton("No", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
}