package com.example.studentregistrationapp

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var studentId: EditText
    lateinit var fullName: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var age: EditText

    lateinit var genderGroup: RadioGroup

    lateinit var football: CheckBox
    lateinit var cricket: CheckBox
    lateinit var basketball: CheckBox
    lateinit var badminton: CheckBox

    lateinit var spinner: Spinner

    lateinit var dateButton: Button
    lateinit var dateText: TextView

    lateinit var submitBtn: Button
    lateinit var resetBtn: Button

    var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        studentId = findViewById(R.id.studentId)
        fullName = findViewById(R.id.fullName)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        age = findViewById(R.id.age)

        genderGroup = findViewById(R.id.genderGroup)

        football = findViewById(R.id.football)
        cricket = findViewById(R.id.cricket)
        basketball = findViewById(R.id.basketball)
        badminton = findViewById(R.id.badminton)

        spinner = findViewById(R.id.countrySpinner)

        dateButton = findViewById(R.id.dateButton)
        dateText = findViewById(R.id.dateText)

        submitBtn = findViewById(R.id.submitBtn)
        resetBtn = findViewById(R.id.resetBtn)

        val countries = arrayOf("Bangladesh", "India", "USA", "UK", "Canada")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)
        spinner.adapter = adapter

        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()

            val dpd = DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedDate = "$day/${month + 1}/$year"
                    dateText.text = selectedDate
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            dpd.show()
        }

        submitBtn.setOnClickListener {

            val id = studentId.text.toString()
            val name = fullName.text.toString()
            val mail = email.text.toString()
            val pass = password.text.toString()
            val ageVal = age.text.toString()

            val genderId = genderGroup.checkedRadioButtonId

            if (id.isEmpty() || name.isEmpty() || mail.isEmpty() || pass.isEmpty() || ageVal.isEmpty() || genderId == -1 || selectedDate == "") {
                Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!mail.contains("@")) {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (ageVal.toInt() <= 0) {
                Toast.makeText(this, "Age must be greater than 0", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val genderRadio = findViewById<RadioButton>(genderId)
            val gender = genderRadio.text.toString()

            var sports = ""

            if (football.isChecked) sports += "Football "
            if (cricket.isChecked) sports += "Cricket "
            if (basketball.isChecked) sports += "Basketball "
            if (badminton.isChecked) sports += "Badminton "

            val country = spinner.selectedItem.toString()

            val message = """
                ID: $id Name: $name Gender: $gender Sports: $sports
                Country: $country
                DOB: $selectedDate
            """.trimIndent()

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        }

        resetBtn.setOnClickListener {

            studentId.text.clear()
            fullName.text.clear()
            email.text.clear()
            password.text.clear()
            age.text.clear()

            genderGroup.clearCheck()

            football.isChecked = false
            cricket.isChecked = false
            basketball.isChecked = false
            badminton.isChecked = false

            spinner.setSelection(0)

            selectedDate = ""
            dateText.text = "No date selected"
        }

    }
}