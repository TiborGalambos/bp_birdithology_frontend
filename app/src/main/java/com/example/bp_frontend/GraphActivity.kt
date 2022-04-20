package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.GraphDataItem
import com.example.bp_frontend.loginLogic.SessionManager
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_graph.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class GraphActivity : AppCompatActivity() {

    data class Occurrence(
        val bird_name:String,
        val bird_count: Int,
    )

    lateinit var sessionManager: SessionManager
    lateinit var apiClient: BackendApiClient

    var monthly_data: ArrayList<BarEntry> = ArrayList()

    private var year_data = ArrayList<Occurrence>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val year_picker = findViewById<EditText>(R.id.year_picker)
        val bird_name = intent.extras?.getString("bird_name")
        val year = intent.extras?.getInt("year")

        val button1 = findViewById<View>(R.id.button1)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = bird_name

        var jan = 0
        var feb = 0
        var mar = 0
        var apr = 0
        var may = 0
        var jun = 0
        var jul = 0
        var aug = 0
        var sep = 0
        var oct = 0
        var nov = 0
        var dec = 0


        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService(this).fetchObservationsForGraph(
            bird_name = bird_name!!,
            year = year!!,
            token = "Token ${sessionManager.getToken()}").enqueue(object : Callback<GraphDataItem?> {
            override fun onResponse(
                call: Call<GraphDataItem?>,
                response: Response<GraphDataItem?>
            ) {
                val items = response.body()




                jan = items!!.year?.jan
                feb = items?.year?.feb
                mar = items?.year?.mar
                apr = items?.year?.apr
                may = items?.year?.maj
                jun = items?.year?.jun
                jul = items?.year?.jul
                aug = items?.year?.aug
                sep = items?.year?.sep
                oct = items?.year?.oct
                nov = items?.year?.nov
                dec = items?.year?.dec

                year_data.add(Occurrence("Jan", jan))
                year_data.add(Occurrence("Feb", feb))
                year_data.add(Occurrence("Mar", mar))
                year_data.add(Occurrence("Apr", apr))
                year_data.add(Occurrence("Máj", may))
                year_data.add(Occurrence("Jún", jun))
                year_data.add(Occurrence("Júl", jul))
                year_data.add(Occurrence("Aug", aug))
                year_data.add(Occurrence("Sep", sep))
                year_data.add(Occurrence("Okt", oct))
                year_data.add(Occurrence("Nov", nov))
                year_data.add(Occurrence("Dec", dec))


                initBarChart()

                //now draw bar chart with dynamic data
                val entries: ArrayList<BarEntry> = ArrayList()

                //you can replace this data object with  your custom object
                for (i in year_data.indices) {
                    val score = year_data[i]
                    entries.add(BarEntry(i.toFloat(), score.bird_count.toFloat()))
                }

                val barDataSet = BarDataSet(entries, "")
                barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

                val data = BarData(barDataSet)
                barChart.data = data

                barChart.invalidate()

            }

            override fun onFailure(call: Call<GraphDataItem?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        button1.setOnClickListener {

            if(year_picker.text.isEmpty())
            {
                year_picker.error = "Zadajte rok"
                year_picker.requestFocus()
                return@setOnClickListener
            }

            if(year_picker.text.toString().toInt() > Calendar.getInstance().get(Calendar.YEAR).toInt())
            {
                year_picker.error = "Nesprávny údaj"
                year_picker.requestFocus()
                return@setOnClickListener
            }
            else
            {
                val intent = Intent(this, GraphActivity::class.java)
                intent.putExtra("bird_name", bird_name)
                intent.putExtra("year", year_picker.text.toString().toInt())
                startActivity(intent)
                finish()
            }


        }

    }

    private fun initBarChart() {


//        hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(3000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -90f

    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < year_data.size) {
                year_data[index].bird_name
            } else {
                ""
            }
        }
    }


    // simulate api call
    // we are initialising it directly

    private fun back_button() {
        val back_button = findViewById<TextView>(R.id.left_top_text)

        back_button.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()
        }
    }




}