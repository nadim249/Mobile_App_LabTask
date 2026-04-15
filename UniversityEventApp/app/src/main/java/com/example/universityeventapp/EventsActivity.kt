package com.example.universityeventapp

import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EventsActivity : AppCompatActivity() {

    private lateinit var adapter: EventAdapter
    private lateinit var allEvents: List<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "All Events"

        val rvEvents: RecyclerView = findViewById(R.id.rvEvents)
        val searchView: SearchView = findViewById(R.id.searchView)
        
        rvEvents.layoutManager = LinearLayoutManager(this)

        allEvents = listOf(
            Event("1", "Tech Symposium 2026", "Oct 12, 2026", "10:00 AM", "Main Auditorium", "Tech", "A grand gathering of tech enthusiasts.", 15.0, 48, 30, R.drawable.img),
            Event("2", "Inter-College Basketball", "Oct 15, 2026", "4:00 PM", "Sports Complex", "Sports", "Annual basketball championship finals.", 5.0, 48, 12, R.drawable.img_2),
            Event("3", "Cultural Fest", "Nov 1, 2026", "6:00 PM", "Open Air Theatre", "Cultural", "Music, dance, and art from across the globe.", 20.0, 48, 48, R.drawable.img),
            Event("4", "AI & ML Workshop", "Nov 5, 2026", "9:00 AM", "Lab 302", "Academic", "Hands-on machine learning workshop.", 10.0, 48, 5, R.drawable.img_2),
            Event("5", "Freshers Mixer", "Sep 10, 2026", "7:00 PM", "Student Lounge", "Social", "Welcome party for new students.", 0.0, 48, 25, R.mipmap.ic_launcher),
            Event("6", "Hackathon 24H", "Oct 20, 2026", "8:00 AM", "Library Hall", "Tech", "24-hour coding challenge with prizes.", 25.0, 48, 8, R.drawable.img),
            Event("7", "Drama Club Play", "Oct 28, 2026", "7:30 PM", "Main Auditorium", "Cultural", "A spectacular rendition of a classic play.", 10.0, 48, 40, R.drawable.img_2),
            Event("8", "Guest Lecture: Physics", "Nov 12, 2026", "2:00 PM", "Seminar Hall A", "Academic", "Renowned physicist discusses quantum mechanics.", 0.0, 48, 15, R.mipmap.ic_launcher)
        )

        adapter = EventAdapter(allEvents)
        rvEvents.adapter = adapter

        setupFilters()
        setupSearch(searchView)
    }

    private fun setupFilters() {
        findViewById<Button>(R.id.btnAll).setOnClickListener { filterByCategory("All") }
        findViewById<Button>(R.id.btnTech).setOnClickListener { filterByCategory("Tech") }
        findViewById<Button>(R.id.btnSports).setOnClickListener { filterByCategory("Sports") }
        findViewById<Button>(R.id.btnCultural).setOnClickListener { filterByCategory("Cultural") }
        findViewById<Button>(R.id.btnAcademic).setOnClickListener { filterByCategory("Academic") }
        findViewById<Button>(R.id.btnSocial).setOnClickListener { filterByCategory("Social") }
    }

    private fun filterByCategory(category: String) {
        val filtered = if (category == "All") allEvents else allEvents.filter { it.category == category }
        adapter.updateData(filtered)
    }

    private fun setupSearch(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = allEvents.filter { 
                    it.title.contains(newText ?: "", ignoreCase = true) || 
                    it.category.contains(newText ?: "", ignoreCase = true) 
                }
                adapter.updateData(filtered)
                return true
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}