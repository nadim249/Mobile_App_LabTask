package com.example.fitnesstrackerapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var steps = 0
    private val goal = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val stepsText = findViewById<TextView>(R.id.stepsValue)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val percentText = findViewById<TextView>(R.id.progressPercent)
        val btn = findViewById<Button>(R.id.updateBtn)
        val dateText = findViewById<TextView>(R.id.dateText)

        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        dateText.text = currentDate

        btn.setOnClickListener {
            val input = EditText(this)

            val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Enter Steps")
                dialog.setView(input)
                dialog.setPositiveButton("Update") { _, _ ->
                    val value = input.text.toString().toIntOrNull()

                    if (value != null) {
                        steps += value
                        stepsText.text = steps.toString()

                        val progress = (steps * 100) / goal
                        progressBar.progress = progress
                        percentText.text = "$progress%"
                        if (progress >= 100) {
                            Toast.makeText(this, "Goal Achieved! 🎉", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                dialog.setNegativeButton("Cancel", null)
                dialog.show()
        }
    }
}