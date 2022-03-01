package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class NewObservationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_observation)

        val textBox = findViewById(R.id.input1) as TextInputLayout
        val dropDown = findViewById(R.id.dropdown_menu) as AutoCompleteTextView

        val items = arrayOf("item1", "item2", "item3")
        val itemAdapter = ArrayAdapter<String>(this, R.layout.items_list, items)

        dropDown.setAdapter(itemAdapter)
        dropDown.setOnItemClickListener { adapterView, view, i, l ->
            textBox.setTextInputAccessibilityDelegate()
            Log.d("items", "$view $i $l")
        }





        val left_top_text = findViewById(R.id.left_top_text) as TextView

        left_top_text.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        val right_top_text = findViewById(R.id.right_top_text) as TextView

        right_top_text.setOnClickListener {
            val intent = Intent(applicationContext, NewSimpleObservationActivity::class.java)
            startActivity(intent)
        }


    }
}