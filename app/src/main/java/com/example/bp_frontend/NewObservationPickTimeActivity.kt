package com.example.bp_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker

class NewObservationPickTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_observation_pick_time)

        val bird_name = intent.getStringExtra("bird_name")
        val file_path = intent.getStringExtra("file_path")
        val number_of_birds = intent.getStringExtra("number_of_birds")

        val clock = findViewById<TimePicker>(R.id.clock)
        val calendar = findViewById<DatePicker>(R.id.calendar)

        clock.setIs24HourView(true)
//        calendar.maxDate = Time.da
//        calendar.



    }
}