package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate


class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_welcome)


        val login_text_button = findViewById(R.id.button_prim) as RelativeLayout

        login_text_button.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK   // clear all activities started before
            startActivity(intent)
        }


        val singup_text_button = findViewById(R.id.secondary_a) as TextView

        singup_text_button.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK   // clear all activities started before
            startActivity(intent)
        }

    }
}