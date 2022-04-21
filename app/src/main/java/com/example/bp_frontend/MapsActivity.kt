package com.example.bp_frontend


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.Locations
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity() {



    lateinit var mapFrag : SupportMapFragment
    lateinit var googleMap: GoogleMap

    var latLngs: List<LatLng?>? = null

    lateinit var sessionManager: SessionManager
    lateinit var apiClient: BackendApiClient

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val bird_name = intent.extras?.getString("bird_name")

        val showHeatmap = intent.extras?.getBoolean("showHeatmap")

        val switch_button = findViewById<View>(R.id.button1)
        val button_text = findViewById<TextView>(R.id.textView3)

        val graphs = findViewById<View>(R.id.button2)

        graphs.setOnClickListener {

        }

        if(showHeatmap == true)
        {
            button_text.text = "Zobraz špendlíky"
        }


        switch_button.setOnClickListener {

            val intent = Intent(this@MapsActivity, MapsActivity::class.java)
            intent.putExtra("bird_name", bird_name)

            if(showHeatmap == true) intent.putExtra("showHeatmap", false)
            if(showHeatmap == false) intent.putExtra("showHeatmap", true)

            startActivity(intent)
            finish()
        }

        val textView = findViewById<TextView>(R.id.textView)

        back_button()

        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService(this@MapsActivity).fetchObservationsForMap(bird_name = bird_name!!, token = "Token ${sessionManager.getToken()}")
            .enqueue(object : Callback<Locations?> {
                override fun onResponse(
                    call: Call<Locations?>,
                    response: Response<Locations?>
                ) {
                    if (response.code() == 200){

                        val data = response.body()

//                        val locations_x = arrayOfNulls<Double>(data!!.obs.size)
//                        val locations_y = arrayOfNulls<Double>(data!!.obs.size)

                        val result: MutableList<LatLng?> = ArrayList()

                        for (i: Int in data!!.obs.indices) {
                            result.add(i, LatLng(data.obs.get(i).obs_y_coords,data.obs.get(i).obs_x_coords))
                            Log.d("my_debug", " second x = ${data.obs.get(i).obs_x_coords} y= ${data.obs.get(i).obs_y_coords}")
                        }
                        latLngs = result

                        mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

                        if(showHeatmap == true){
                            val provider = HeatmapTileProvider.Builder()
                                .data(latLngs)
                                .build()
                            mapFrag.getMapAsync(OnMapReadyCallback {
                                googleMap = it
                                val slovakia = LatLng(48.669025, 19.699024)
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(slovakia, 6.5f))
                                val overlay = googleMap.addTileOverlay(TileOverlayOptions().tileProvider(provider))
                            })
                        }

                        if(showHeatmap == false){

                            mapFrag.getMapAsync(OnMapReadyCallback {
                                googleMap = it

                                val slovakia = LatLng(48.669025, 19.699024)

                                for (i: Int in data.obs.indices) {
                                    googleMap.addMarker(MarkerOptions().position(LatLng(data.obs.get(i).obs_y_coords,data.obs.get(i).obs_x_coords)).title("Počet: ${data.obs.get(i).bird_count}"))
                                }
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(slovakia, 6.5f))
                            })
                        }
                        textView.text = bird_name
                    }
                }

                override fun onFailure(call: Call<Locations?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun back_button() {
        val back_button = findViewById<TextView>(R.id.left_top_text)

        back_button.setOnClickListener {
            val intent = Intent(this, BirdSpeciesStatsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()
        }
    }
}