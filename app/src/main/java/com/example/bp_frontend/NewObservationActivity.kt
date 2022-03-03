package com.example.bp_frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bp_frontend.ebirdEndpoint.*
import com.example.myapplication.models.EbirdBirdModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import res.layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.system.exitProcess

const val BASE_URL = "https://api.ebird.org/v2/"

class NewObservationActivity : AppCompatActivity() {

    val items: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_observation)
        Log.d("---","before load data in main")
        val job1 = launch { loadData() }

        val job2 = launch {
            delay(50L)
            Log.d("---","main launch start")
            val autoCompleteTextView = findViewById(R.id.dropdown_menu) as AutoCompleteTextView
            val adapter = ArrayAdapter(this@NewObservationActivity, android.R.layout.simple_list_item_1, items)
            autoCompleteTextView.threshold = 0
            autoCompleteTextView.setAdapter(adapter)
            autoCompleteTextView.setOnFocusChangeListener { view, b -> if (b) autoCompleteTextView.showDropDown()
            Log.d("---","main launch end")}
        }

        job1.join()
        job2.join()

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

    private fun loadData() {
        Log.d("---","before load data")
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<EbirdDataItem>?> {
            override fun onResponse(
                call: Call<List<EbirdDataItem>?>,
                response: Response<List<EbirdDataItem>?>
            ) {
                val responseBody = response.body()!!

                for (myData in responseBody) {
                    items.add(myData.comName)
                }
                Log.d("---","after for cycle in load data")
            }
            override fun onFailure(call: Call<List<EbirdDataItem>?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        Log.d("---","after load data")

    }
}