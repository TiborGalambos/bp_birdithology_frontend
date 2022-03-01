package com.example.bp_frontend

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val login_text_button = findViewById(R.id.right_top_text) as TextView

        login_text_button.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }


        val cancel_button = findViewById(R.id.left_top_text) as TextView

        cancel_button.setOnClickListener {
            val intent = Intent(applicationContext, WelcomeActivity::class.java)

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
        }


        val button_signup = findViewById(R.id.button_signup) as View

        button_signup.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }


    }

}

