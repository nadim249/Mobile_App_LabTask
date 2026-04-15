package com.example.gradereportapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private lateinit var etSubject: EditText
    private lateinit var etObtained: EditText
    private lateinit var etTotal: EditText
    private lateinit var btnAdd: Button
    private lateinit var tvGPA: TextView
    private lateinit var tvSummary: TextView

    private var totalSubjects = 0
    private var passed = 0
    private var failed = 0
    private var totalGPAPoints = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tableLayout = findViewById(R.id.tableLayout)
        etSubject = findViewById(R.id.etSubjectName)
        etObtained = findViewById(R.id.etObtained)
        etTotal = findViewById(R.id.etTotal)
        btnAdd = findViewById(R.id.btnAdd)
        tvGPA = findViewById(R.id.tvGPA)
        tvSummary = findViewById(R.id.tvSummary)


        addSubjectRow("Mathematics", 95, 100)
        addSubjectRow("Physics", 82, 100)
        addSubjectRow("Chemistry", 75, 100)
        addSubjectRow("English", 65, 100)
        addSubjectRow("History", 35, 100)
        addSubjectRow("Programming", 88, 100)

        btnAdd.setOnClickListener {
            val name = etSubject.text.toString()
            val obtained = etObtained.text.toString().toIntOrNull()
            val total = etTotal.text.toString().toIntOrNull()

            if (name.isNotEmpty() && obtained != null && total != null) {
                addSubjectRow(name, obtained, total)
                etSubject.text.clear()
                etObtained.text.clear()
                etTotal.text.clear()
            } else {
                Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
            }
    }
}

    private fun addSubjectRow(name: String, obtained: Int, total: Int) {
        val percentage = (obtained.toDouble() / total.toDouble()) * 100
        val (grade, gpaPoint) = calculateGradeAndGPA(percentage)

        val tableRow = TableRow(this)
        tableRow.setPadding(8, 16, 8, 16)


        val bgColor = if (grade == "F") ContextCompat.getColor(this, R.color.fail_red)
        else ContextCompat.getColor(this, R.color.pass_green)
        tableRow.setBackgroundColor(bgColor)


        tableRow.addView(createTextView(name))
        tableRow.addView(createTextView(obtained.toString()))
        tableRow.addView(createTextView(total.toString()))
        tableRow.addView(createTextView(grade))

        tableLayout.addView(tableRow)


        totalSubjects++
        if (grade == "F") failed++ else passed++
        totalGPAPoints += gpaPoint

        updateSummary()
    }

    private fun createTextView(text: String): TextView {
        val tv = TextView(this)
        tv.text = text
        tv.gravity = android.view.Gravity.CENTER
        tv.setTextColor(Color.BLACK)
        return tv
    }

    private fun calculateGradeAndGPA(score: Double): Pair<String, Double> {
        return when {
            score >= 90 -> "A+" to 4.0
            score >= 80 -> "A" to 3.7
            score >= 70 -> "B+" to 3.3
            score >= 60 -> "B" to 3.0
            score >= 50 -> "C" to 2.0
            score >= 40 -> "D" to 1.0
            else -> "F" to 0.0
        }
    }

    private fun updateSummary() {
        val finalGPA = if (totalSubjects > 0) totalGPAPoints / totalSubjects
        else 0.0
        tvGPA.text = "GPA: %.2f".format(finalGPA)
        tvSummary.text = "Total Subjects: $totalSubjects | Passed: $passed | Failed: $failed"
    }
}