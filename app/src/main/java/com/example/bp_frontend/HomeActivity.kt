package com.example.bp_frontend

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bp_frontend.ListAdapter.ItemListAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.backendEndpoints.LoginResponse
import com.example.bp_frontend.dataItems.ObservationList
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ContextUtils.getActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient
    lateinit var id:Array<String?>
    var item_id:Int = 0




    @SuppressLint("RtlHardcoded", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        




        logoutButtonListener()

        val right_top_text = findViewById<TextView>(R.id.right_top_text)
        right_top_text.setOnClickListener{
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)

        }

        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)


        // --------------------------------
        // Fetch Observations With Comments
        // --------------------------------

        apiClient.getApiService(this@HomeActivity).fetchObservationsWithComments(token = "Token ${sessionManager.getToken()}")
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

        // ----------------------
        // Floating Action Button
        // ----------------------
        startNewObservation()
    }

    private fun logoutButtonListener() {
        val left_top_text = findViewById<TextView>(R.id.left_top_text)
        left_top_text.setOnClickListener {

            apiClient.getApiService(this).logOut(token = "Token ${sessionManager.getToken()}")
                .enqueue(object : Callback<LoginResponse?> {
                    override fun onResponse(
                        call: Call<LoginResponse?>,
                        response: Response<LoginResponse?>
                    ) {
                        if (response.code() == 204) {
                            val intent = Intent(applicationContext, WelcomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        if (response.code() == 401) {
                            Toast.makeText(
                                applicationContext,
                                "Something went wrong, please report this to Tibor Galambos",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    private fun fetchObservations(response: Response<ObservationList?>) {
        val items = response.body()

        val author = arrayOfNulls<String>(items!!.obs.size)
        val bird_name = arrayOfNulls<String>(items.obs.size)
        val bird_count = arrayOfNulls<String>(items.obs.size)
        val photo = arrayOfNulls<String>(items.obs.size)
        val id = arrayOfNulls<String>(items.obs.size)



        for (i: Int in items.obs.indices)
        {
            author[i] = (items.obs[i].obs_author).replace("\"", "")
            bird_name[i] = (items.obs[i].bird_name).replace("\"", "")
            bird_count[i] = ((items.obs[i].bird_count).toString().replace("\"", ""))
            photo[i] = items.obs[i].bird_photo
            id[i] = items.obs[i].id.toString()
            Log.d("my_debug", "$i ${items.obs.size}  ${items.obs[i].comments.indices}")
        }

//        for (i: Int in items.obs.indices)
//        {
//            for (j: Int in items.obs[i].comments.indices)
//            {
//                Log.d("my_debug", "$i $j ${comment.size}")
//                com_author[i][j] = (items.obs[i].comments[j].com_author).replace("\"", "")
//                comment[i][j] = (items.obs[i].comments[j].comment).replace("\"", "")
//            }
//        }



        val adapter = ItemListAdapter(
            this@HomeActivity,
            author,
            bird_name,
            bird_count,
            photo,
            id,
            items,
//            com_author,
//            comment
        )
        val list_id = findViewById<ListView>(R.id.list_id)
        list_id.adapter = adapter


        list_id.setOnItemClickListener{ _, _, position, _ ->

            val idOfSelectedItem = id[position]

            Log.d("NUMBER: ", "$idOfSelectedItem $ ${items.obs[position].comments.indices}")

            val intent = Intent(this, ItemContent::class.java)

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

            intent.putExtra("obs_author", items.obs[position].obs_author)
            intent.putExtra("bird_count", items.obs[position].bird_count)
            intent.putExtra("bird_name", items.obs[position].bird_name)
            intent.putExtra("obs_x_coords", items.obs[position].obs_x_coords)
            intent.putExtra("obs_y_coords", items.obs[position].obs_y_coords)


            startActivity(intent)
        }



//        val comments_id = findViewById<ListView>(R.id.comments_id)
//
////        val com_adapter = CommentListAdapter(
////            context,
////            com_author,
////            comment
////        )
//
//        comments_id.adapter = com_adapter



    }

    private fun startNewObservation() {
        val floatingActionButton = findViewById(R.id.floatingActionButton) as FloatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(applicationContext, NewObservationActivity::class.java)
            startActivity(intent)
        }
    }
}