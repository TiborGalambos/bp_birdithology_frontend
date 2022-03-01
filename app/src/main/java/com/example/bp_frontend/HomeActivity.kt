package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.example.bp_frontend.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



//        floatingActionButton
        val floatingActionButton = findViewById(R.id.floatingActionButton) as com.google.android.material.floatingactionbutton.FloatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(applicationContext, NewObservationActivity::class.java)
            startActivity(intent)
        }

    }
}