package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.EbirdDataItem
import com.example.bp_frontend.dataItems.GlobStatsSum
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class GlobalStatisticsActivity : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_statistics)


        val obs_sum = findViewById<TextView>(R.id.obs_sum)
        val obs_sum_personal = findViewById<TextView>(R.id.obs_sum_personal)
        val button = findViewById<View>(R.id.button1)
        val back_button = findViewById<TextView>(R.id.left_top_text)

        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService(this@GlobalStatisticsActivity).fetchStatsSumPersonal(token = "Token ${sessionManager.getToken()}").enqueue(object : Callback<GlobStatsSum?> {
            override fun onResponse(call: Call<GlobStatsSum?>, response: Response<GlobStatsSum?>) {

                if (response.code() == 200)
                {
                    Log.d("my_debug", "${response.body()?.sum?.bird_count__sum}")
                    obs_sum_personal.text = response.body()?.sum?.bird_count__sum.toString()
                }
            }

            override fun onFailure(call: Call<GlobStatsSum?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        apiClient.getApiService(this@GlobalStatisticsActivity).fetchStatsSum(token = "Token ${sessionManager.getToken()}").enqueue(object : Callback<GlobStatsSum?> {
            override fun onResponse(call: Call<GlobStatsSum?>, response: Response<GlobStatsSum?>) {
                if (response.code() == 200)
                {
                    Log.d("my_debug", "${response.body()?.sum?.bird_count__sum}")
                    obs_sum.text = response.body()?.sum?.bird_count__sum.toString()
                }
            }

            override fun onFailure(call: Call<GlobStatsSum?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })


        button.setOnClickListener{
            val intent = Intent(this@GlobalStatisticsActivity, BirdSpeciesStatsActivity::class.java)
            startActivity(intent)
        }

        back_button.setOnClickListener {
            val intent = Intent(this@GlobalStatisticsActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()
        }




//        val dropdown_menu = findViewById(R.id.dropdown_menu_glob_stat) as AutoCompleteTextView
//        dropdown_menu.isVisible = false
//        dropdown_menu.inputType = 0
//
//        val loading = findViewById<TextView>(R.id.loading_glob_stat)
//        loading.isVisible = true
//
//        loadData(dropdown_menu, loading)
//        setBirdList()


    }

//    private fun setBirdList() {
//        val autoCompleteTextView = findViewById(R.id.dropdown_menu_glob_stat) as AutoCompleteTextView
//        val adapter =
//            ArrayAdapter(this@GlobalStatisticsActivity, android.R.layout.simple_list_item_1, items)
//        autoCompleteTextView.threshold = 0
//        autoCompleteTextView.setAdapter(adapter)
//        autoCompleteTextView.setOnFocusChangeListener { _, b -> if (b) autoCompleteTextView.showDropDown() }
//    }
//
//
//    private fun loadData(dropdown_menu: AutoCompleteTextView, loading: TextView) {
//        val retrofitBuilder = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//            .build()
//            .create(ApiInterface::class.java)
//
//        val retrofitData = retrofitBuilder.getData()
//
//        retrofitData.enqueue(object : Callback<List<EbirdDataItem>?> {
//            override fun onResponse(
//                call: Call<List<EbirdDataItem>?>,
//                response: Response<List<EbirdDataItem>?>
//            ) {
//                val responseBody = response.body()!!
//
//                if(response.code() == 200){
//                    for (myData in responseBody) {
//                        items.add(myData.comName)
//                    }
//                    dropdown_menu.isVisible = true
//                    loading.isVisible = false
//                }
//
//            }
//            override fun onFailure(call: Call<List<EbirdDataItem>?>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }

}