package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.bp_frontend.ListAdapter.MyCollectionAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.ObservationList
import com.example.bp_frontend.loginLogic.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCollectionActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient
    lateinit var ids:Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_collection)

        back_button()

        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        val page_number = intent.getIntExtra("page_number",1)


        apiClient.getApiService(this).fetchUserObservations(page_number = page_number, token = "Token ${sessionManager.getToken()}")
            .enqueue(object : Callback<ObservationList?> {
                override fun onResponse(
                    call: Call<ObservationList?>,
                    response: Response<ObservationList?>
                ) {
                    if(response.code() == 200){
                        fetchObservations(response)

                    }
                    if(response.code() != 200)
                        Toast.makeText(applicationContext, "HALF GOOD ${response.code()}", Toast.LENGTH_SHORT)
                }

                override fun onFailure(call: Call<ObservationList?>, t: Throwable) {
                    Toast.makeText(applicationContext, "VERY BAD", Toast.LENGTH_SHORT)
                }
            })





    }

    private fun fetchObservations(response: Response<ObservationList?>) {
        val items = response.body()!!

        val bird_name = arrayOfNulls<String>(items.obs.size)
        val bird_count = arrayOfNulls<String>(items.obs.size)
        val obs_location = arrayOfNulls<String>(items.obs.size)
        val photo = arrayOfNulls<String>(items.obs.size)
        val id = arrayOfNulls<String>(items.obs.size)



        for (i: Int in items.obs.indices)
        {
            bird_name[i] = (items.obs[i].bird_name).replace("\"", "")
            bird_count[i] = ((items.obs[i].bird_count).toString().replace("\"", ""))
            photo[i] = items.obs[i].bird_photo
            obs_location[i] = (items.obs[i].obs_place).replace("\"", "")
            id[i] = items.obs[i].id.toString()

        }


        val adapter = MyCollectionAdapter(
            this@MyCollectionActivity,
            bird_name = bird_name,
            bird_count =  bird_count,
            photo = photo,
            id = id,
            location = obs_location
        )
        val list_id = findViewById<ListView>(R.id.list_id_my_collection)
        list_id.adapter = adapter


        val next_button = findViewById<View>(R.id.next_button)
        val prev_button = findViewById<View>(R.id.prev_button)

        val next_image = findViewById<ImageView>(R.id.next_image)
        val prev_image = findViewById<ImageView>(R.id.prev_image)

        val intent_page = Intent(this, MyCollectionActivity::class.java)


        if(response.body()!!.paginator.has_next){
            next_button.isVisible = true
            next_image.isVisible = true

            next_button.setOnClickListener{
                intent_page.putExtra("page_number", (response.body()!!.paginator.current_page.toInt()+1).toInt())
                startActivity(intent_page)
                finish()
            }
        }

        if(response.body()!!.paginator.has_prev){
            prev_button.isVisible = true
            prev_image.isVisible = true

            prev_button.setOnClickListener {
                intent_page.putExtra("page_number", (response.body()!!.paginator.current_page.toInt()-1).toInt())
                startActivity(intent_page)
                finish()
            }
        }




        list_id.setOnItemClickListener{ _, _, position, _ ->


            val intent = Intent(this, PersonalItemContent::class.java)

            val com_author = arrayOfNulls<String>(size = items.obs[position].comments.size)
            val comment = arrayOfNulls<String>(size = items.obs[position].comments.size)

            for (j: Int in items.obs[position].comments.indices)
            {
                com_author[j] = (items.obs[position].comments[j].com_author)
                comment[j] = (items.obs[position].comments[j].comment)
            }

            intent.putExtra("com_author", com_author)
            intent.putExtra("comment", comment)

            intent.putExtra("id",items.obs[position].id)

            intent.putExtra("obs_location", (items.obs[position].obs_place))

            intent.putExtra("obs_author", items.obs[position].obs_author)
            intent.putExtra("bird_count", items.obs[position].bird_count)
            intent.putExtra("bird_name", items.obs[position].bird_name)
            intent.putExtra("photo", items.obs[position].bird_photo)




            startActivity(intent)
        }


    }

    private fun back_button() {
        val left_top_text = findViewById<TextView>(R.id.left_top_text)
        left_top_text.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()

        }
    }
}