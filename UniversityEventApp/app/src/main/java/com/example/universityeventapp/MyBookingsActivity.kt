package com.example.universityeventapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyBookingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        val rvMyBookings: RecyclerView = findViewById(R.id.rvMyBookings)
        val tvNoBookings: TextView? = findViewById(R.id.tvNoBookings)

        val bookedEvents = BookingManager.getBookedEvents()

        if (bookedEvents.isEmpty()) {
            tvNoBookings?.visibility = View.VISIBLE
        } else {
            tvNoBookings?.visibility = View.GONE
            rvMyBookings.layoutManager = LinearLayoutManager(this)
            rvMyBookings.adapter = EventAdapter(bookedEvents)
        }
    }
}