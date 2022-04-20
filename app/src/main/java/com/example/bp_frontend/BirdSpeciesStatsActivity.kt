package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.bp_frontend.ListAdapter.BirdCountListAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.BirdSpeciesStats
import com.example.bp_frontend.dataItems.Locations
import com.example.bp_frontend.dataItems.ObservationList
import com.example.bp_frontend.loginLogic.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BirdSpeciesStatsActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient
    lateinit var ids:Array<String?>

//    lateinit var locations_x: DoubleArray
//    lateinit var locations_y: DoubleArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bird_species_stats)


        val back_button = findViewById<TextView>(R.id.left_top_text)

        back_button.setOnClickListener {
            val intent = Intent(this@BirdSpeciesStatsActivity, GlobalStatisticsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()
        }


        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService(this@BirdSpeciesStatsActivity).fetchBirdStatsSum(token = "Token ${sessionManager.getToken()}")
            .enqueue(object : Callback<BirdSpeciesStats?> {
                override fun onResponse(
                    call: Call<BirdSpeciesStats?>,
                    response: Response<BirdSpeciesStats?>
                ) {
                    if(response.code() == 200){
                        fetchObservations(response)

                    }
                    if(response.code() != 200)
                        Toast.makeText(applicationContext, "HALF GOOD ${response.code()}", Toast.LENGTH_SHORT)
                }

                override fun onFailure(call: Call<BirdSpeciesStats?>, t: Throwable) {
                    Toast.makeText(applicationContext, "VERY BAD", Toast.LENGTH_SHORT)
                }
            })

    }

    private fun fetchObservations(response: Response<BirdSpeciesStats?>) {
        val items = response.body()



        val bird_name = arrayOfNulls<String>(items!!.birds.size)
        for (i: Int in items!!.birds.indices)
            bird_name[i] = (items.birds[i].bird_name).replace("\"", "")

        val bird_count = arrayOfNulls<String>(items!!.birds.size)
        for (i: Int in items!!.birds.indices)
            bird_count[i] = ((items.birds[i].bird_count).toString().replace("\"", ""))


        val adapter = BirdCountListAdapter(
            this,
            bird_name,
            bird_count
        )
        val list_id = findViewById<ListView>(R.id.list_id_spec)
        list_id.adapter = adapter


        list_id.setOnItemClickListener{ _, _, position, _ ->

            val birdName = items.birds[position].bird_name.replace("\"", "")

            val intent2 = Intent(this@BirdSpeciesStatsActivity, MapsActivity::class.java)
            intent2.putExtra("bird_name", birdName)
            intent2.putExtra("showHeatmap", false)
            startActivity(intent2)

        }

    }

}