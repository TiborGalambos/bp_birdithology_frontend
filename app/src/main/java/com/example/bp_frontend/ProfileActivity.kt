package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.bp_frontend.ListAdapter.SmallItemListAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.ObservationList
import com.example.bp_frontend.loginLogic.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient
    lateinit var ids:Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val left_top_text = findViewById<TextView>(R.id.left_top_text)
        left_top_text.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()

        }



        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        val profile_name = findViewById<TextView>(R.id.profile_name)
        profile_name.text = sessionManager.getUsername().toString()

        val account_type = findViewById<TextView>(R.id.account_type)
        account_type.text = sessionManager.getUserType().toString()



//        apiClient.getApiService(this@ProfileActivity).fetchUserObservations(page_number = 1, token = "Token ${sessionManager.getToken()}")
//            .enqueue(object : Callback<ObservationList?> {
//                override fun onResponse(
//                    call: Call<ObservationList?>,
//                    response: Response<ObservationList?>
//                ) {
//                    if(response.code() == 200){
//                        fetchObservations(response)
//
//                    }
//                    if(response.code() != 200)
//                        Toast.makeText(applicationContext, "HALF GOOD ${response.code()}", Toast.LENGTH_SHORT)
//                }
//
//                override fun onFailure(call: Call<ObservationList?>, t: Throwable) {
//                    Toast.makeText(applicationContext, "VERY BAD", Toast.LENGTH_SHORT)
//                }
//            })







    }

    private fun fetchObservations(response: Response<ObservationList?>) {
        val items = response.body()

        val author = arrayOfNulls<String>(items!!.obs.size)
        for (i: Int in items.obs.indices)
            author[i] = (items.obs[i].obs_author).replace("\"", "")

        val bird_name = arrayOfNulls<String>(items.obs.size)
        for (i: Int in items.obs.indices)
            bird_name[i] = (items.obs[i].bird_name).replace("\"", "")

        val bird_count = arrayOfNulls<String>(items.obs.size)
        for (i: Int in items.obs.indices)
            bird_count[i] = ((items.obs[i].bird_count).toString().replace("\"", ""))

        val photo = arrayOfNulls<String>(items.obs.size)
        for (i: Int in items.obs.indices)
            photo[i] = items.obs[i].bird_photo

        val id = arrayOfNulls<String>(items.obs.size)
        for (i: Int in items.obs.indices)
            id[i] = items.obs[i].id.toString()

        val adapter = SmallItemListAdapter(
            this,
            author,
            bird_name,
            bird_count,
            photo,
            id,
        )
        val list_id = findViewById<ListView>(R.id.list_id)
        list_id.adapter = adapter
    }
}