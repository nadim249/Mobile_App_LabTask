package com.example.photogalleryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var adapter: PhotoAdapter
    private var allPhotos = mutableListOf<Photo>()
    private var isSelectionMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        gridView = findViewById(R.id.gridView)
        
        // Populate the photo list
        setupData()
        
        adapter = PhotoAdapter(this, allPhotos)
        gridView.adapter = adapter

        // Item Clicks
        gridView.setOnItemClickListener { _, _, position, _ ->
            val photo = adapter.getItem(position)
            if (isSelectionMode) {
                photo.isSelected = !photo.isSelected
                updateSelectionUI()
            } else {
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("IMG_RES", photo.resourceId)
                startActivity(intent)
            }
        }

        // Long Press to Enter Selection Mode
        gridView.setOnItemLongClickListener { _, _, position, _ ->
            if (!isSelectionMode) {
                isSelectionMode = true
                adapter.isSelectionMode = true
                allPhotos[position].isSelected = true
                updateSelectionUI()
                return@setOnItemLongClickListener true
            }
            false
        }

        // Filtering Logic
        findViewById<Button>(R.id.btnAll).setOnClickListener { filterPhotos("All") }
        findViewById<Button>(R.id.btnNature).setOnClickListener { filterPhotos("Nature") }
        findViewById<Button>(R.id.btnCity).setOnClickListener { filterPhotos("City") }
        findViewById<Button>(R.id.btnAnimals).setOnClickListener { filterPhotos("Animal") }
        findViewById<Button>(R.id.btnFoods).setOnClickListener { filterPhotos("Food") }
        findViewById<Button>(R.id.btnTravels).setOnClickListener { filterPhotos("Travel") }

        // Delete Logic
        findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
            val selected = allPhotos.filter { it.isSelected }
            allPhotos.removeAll(selected)
            exitSelectionMode()
            Toast.makeText(this, "${selected.size} photos deleted", Toast.LENGTH_SHORT).show()
        }

        // FAB to Add Photo
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            allPhotos.add(Photo(allPhotos.size + 1, R.drawable.sample_image, "New Photo", "Nature"))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupData() {
        val imageData = listOf(
            "nature1" to "Nature", "nature2" to "Nature",
            "city1" to "City", "city2" to "City", "city3" to "City",
            "animal1" to "Animal", "animal2" to "Animal", "animal3" to "Animal",
            "food1" to "Food", "food2" to "Food", "food3" to "Food",
            "travel1" to "Travel", "travel2" to "Travel"
        )

        for ((index, data) in imageData.withIndex()) {
            val fileName = data.first
            val categoryName = data.second

            // This line converts the String file name into a resource ID (Int)
            val resId = resources.getIdentifier(fileName, "drawable", packageName)

            // Add to our photo list
            allPhotos.add(
                Photo(
                    id = index + 1,
                    resourceId = resId,
                    title = "${categoryName} ${index + 1}",
                    category = categoryName
                )
            )
        }
    }

    private fun filterPhotos(category: String) {
        val filteredList = if (category == "All") allPhotos else allPhotos.filter { it.category == category }
        adapter.updateList(filteredList)
    }

    private fun updateSelectionUI() {
        val count = allPhotos.count { it.isSelected }
        findViewById<LinearLayout>(R.id.selectionToolbar).visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        findViewById<TextView>(R.id.tvSelectionCount).text = "$count Selected"
        adapter.notifyDataSetChanged()
    }

    private fun exitSelectionMode() {
        isSelectionMode = false
        adapter.isSelectionMode = false
        allPhotos.forEach { it.isSelected = false }
        updateSelectionUI()
    }
}
