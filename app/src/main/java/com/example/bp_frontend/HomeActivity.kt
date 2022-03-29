package com.example.bp_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bp_frontend.ListAdapter.ItemListAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.RecentObservationsDataItem
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient
    lateinit var ids:Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService(this).fetchNormalObservations(token = "Token ${sessionManager.getToken()}")
            .enqueue(object : Callback<RecentObservationsDataItem?> {
                override fun onResponse(
                    call: Call<RecentObservationsDataItem?>,
                    response: Response<RecentObservationsDataItem?>
                ) {
                    if(response.code() == 200){
                        val items = response.body()

                        val author = arrayOfNulls<String>(items!!.obs.size)
                        for (i: Int in items.obs.indices)
                            author[i] = (items.obs[i].obs_author_name).replace("\"", "")

                        val bird_name = arrayOfNulls<String>(items.obs.size)
                        for (i: Int in items.obs.indices)
                            bird_name[i] = (items.obs[i].bird_name).toString()

                        val bird_count = arrayOfNulls<String>(items.obs.size)
                        for (i: Int in items.obs.indices)
                            bird_count[i] = ((items.obs[i].bird_count).toString().replace("\"", ""))

                        val photo = arrayOfNulls<String>(items.obs.size)
                        for (i: Int in items.obs.indices)
                            photo[i] = items.obs[i].bird_photo

                        val adapter = ItemListAdapter(
                            this@HomeActivity,
                            author,
                            bird_name,
                            bird_count,
                            photo,
                        )
                        val list_id = findViewById<ListView>(R.id.list_id)
                        list_id.adapter = adapter

    //                    ids = arrayOfNulls<String>(items!!.obs!!.size)
    //                    for (i: Int in items.obs.indices)
    //                        ids[i] = (items.obs[i].id).toString()
                    }
                    if(response.code() != 200)
                        Toast.makeText(applicationContext, "HALF GOOD ${response.code()}", Toast.LENGTH_SHORT)
                }

                override fun onFailure(call: Call<RecentObservationsDataItem?>, t: Throwable) {
                    Toast.makeText(applicationContext, "VERY BAD", Toast.LENGTH_SHORT)
                }
            })




        startNewObservation() // floatingActionButton
    }

    private fun startNewObservation() {
        val floatingActionButton = findViewById(R.id.floatingActionButton) as FloatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(applicationContext, NewObservationActivity::class.java)
            startActivity(intent)
        }
    }
}