 package com.example.loginprofileapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var loginBtn : Button
    lateinit var logoutBtn : Button
    lateinit var progressBar : ProgressBar
    lateinit var profileCard : LinearLayout
    lateinit var logo: ImageView
    lateinit var title: TextView
    lateinit var forget: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.loginBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        progressBar = findViewById(R.id.progressBar)
        profileCard = findViewById(R.id.profileCard)
        logo = findViewById(R.id.logo)
        title = findViewById(R.id.title)
        forget = findViewById(R.id.forgetPassword)

        loginBtn.setOnClickListener {
            val user=username.text.toString()
            val pass=password.text.toString()

            if(user=="admin" && pass=="1234"){
                progressBar.visibility= View.VISIBLE

                Handler().postDelayed({
                    progressBar.visibility= View.GONE


                    logo.visibility = View.GONE
                    title.visibility = View.GONE
                    username.visibility = View.GONE
                    password.visibility = View.GONE
                    loginBtn.visibility = View.GONE
                    forget.visibility = View.GONE

                    profileCard.visibility= View.VISIBLE
                },2000)

            }else {
                Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_SHORT).show()
            }
        }

        logoutBtn.setOnClickListener {

            profileCard.visibility = View.GONE

            logo.visibility = View.VISIBLE
            title.visibility = View.VISIBLE
            username.visibility = View.VISIBLE
            password.visibility = View.VISIBLE
            loginBtn.visibility = View.VISIBLE
            forget.visibility = View.VISIBLE

            username.text.clear()
            password.text.clear()
        }

        forget.setOnClickListener {
            Toast.makeText(this,
                "Password reset link sent to your email",
                Toast.LENGTH_LONG).show()
        }



    }
}