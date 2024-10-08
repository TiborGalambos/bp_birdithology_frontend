package com.example.bp_frontend


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bp_frontend.ListAdapter.ItemListAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.backendEndpoints.LoginResponse
import com.example.bp_frontend.dataItems.AdminVerification
import com.example.bp_frontend.dataItems.ObservationList
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient
    lateinit var id:Array<String?>
    var item_id:Int = 0

    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var navigationView: NavigationView? = null

    var admin_menu : MenuItem? = null


    @SuppressLint("RtlHardcoded", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val drawer_menu = findViewById(R.id.navigation_menu) as NavigationView

        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        amIAdmin(drawer_menu)

        setUpToolbar()
        navigationView = findViewById<View>(R.id.navigation_menu) as NavigationView
        navigationView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this@HomeActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.nav_profile -> {
                    val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_logout -> {
                    logOutUser()
                }

                R.id.nav_new_obs -> {
                    val intent = Intent(this@HomeActivity, NewObservationActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_glob_stats -> {
                    val intent = Intent(this@HomeActivity, GlobalStatisticsActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_my_collection -> {
                    val intent = Intent(this@HomeActivity, MyCollectionActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_all_obs -> {
                    val intent = Intent(this@HomeActivity, AllConfirmedObservationsActivity::class.java)
                    startActivity(intent)
                }

                admin_menu?.itemId -> {
                    val intent = Intent(this@HomeActivity, AdminConfirmActivity::class.java)
                    startActivity(intent)
                }

            }
            false
        }




        val left_top_text = findViewById<ImageButton>(R.id.left_top_text)
        left_top_text.setOnClickListener {

            if(!drawerLayout!!.isOpen) {
                drawerLayout!!.openDrawer(Gravity.LEFT)
            }
            else {
                drawerLayout!!.closeDrawer(Gravity.LEFT)
            }
        }

        val right_top_text = findViewById<TextView>(R.id.right_top_text)
        right_top_text.setOnClickListener{
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)

        }





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
                    Toast.makeText(applicationContext, "GOOD ${response.code()}", Toast.LENGTH_SHORT)
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

    private fun amIAdmin(drawer_menu: NavigationView) {
        apiClient.getApiService(this@HomeActivity)
            .amIAdmin(token = "Token ${sessionManager.getToken()}")
            .enqueue(object : Callback<AdminVerification?> {
                override fun onResponse(
                    call: Call<AdminVerification?>,
                    response: Response<AdminVerification?>
                ) {
                    Log.d("my_debug", "${response.body()?.is_admin}")
                    if (response.code() == 200 && response.body()!!.is_admin) {
                        admin_menu = drawer_menu.menu.add("Nepotvrdené pozorovania")

                        sessionManager.saveUserType("Odborník")

                    }
                    else sessionManager.saveUserType("Používateľ")
                }

                override fun onFailure(call: Call<AdminVerification?>, t: Throwable) {
                    Toast.makeText(applicationContext, "VERY BAD", Toast.LENGTH_SHORT)
                }
            })
    }

    private fun logOutUser() {
        apiClient = BackendApiClient()
        sessionManager = SessionManager(this@HomeActivity)

        apiClient.getApiService(this).logOut(token = "Token ${sessionManager.getToken()}")
            .enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    if (response.code() == 204) {
                        val intent = Intent(applicationContext, WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
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

    fun setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        toolbar.setNavigationIcon(null);
    }






    private fun fetchObservations(response: Response<ObservationList?>) {
        val items = response.body()

        val author = arrayOfNulls<String>(items!!.obs.size)
        val bird_name = arrayOfNulls<String>(items.obs.size)
        val bird_count = arrayOfNulls<String>(items.obs.size)
        val obs_location = arrayOfNulls<String>(items.obs.size)
        val photo = arrayOfNulls<String>(items.obs.size)
        val id = arrayOfNulls<String>(items.obs.size)



        for (i: Int in items.obs.indices)
        {
            author[i] = (items.obs[i].obs_author).replace("\"", "")
            bird_name[i] = (items.obs[i].bird_name).replace("\"", "")
            bird_count[i] = ((items.obs[i].bird_count).toString().replace("\"", ""))
            photo[i] = items.obs[i].bird_photo
            obs_location[i] = (items.obs[i].obs_place).replace("\"", "")
            id[i] = items.obs[i].id.toString()
//            Log.d("my_debug", "$i ${items.obs.size}  ${items.obs[i].comments.indices}")
        }


        val adapter = ItemListAdapter(
            this@HomeActivity,
            author = author,
            bird_name = bird_name,
            bird_count =  bird_count,
            photo = photo,
            id = id,
            items = items,
            location = obs_location

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

            intent.putExtra("obs_location", (items.obs[position].obs_place))

            intent.putExtra("obs_author", items.obs[position].obs_author)
            intent.putExtra("bird_count", items.obs[position].bird_count)
            intent.putExtra("bird_name", items.obs[position].bird_name)
            intent.putExtra("photo", items.obs[position].bird_photo)


            Log.d("my_debug", "obs_x_coords: ${items.obs[position].obs_x_coords}" +
                    "obs_y_coords: ${items.obs[position].obs_y_coords}")


            startActivity(intent)
        }

    }

    private fun startNewObservation() {
        val floatingActionButton = findViewById(R.id.floatingActionButton) as FloatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(applicationContext, NewObservationActivity::class.java)
            startActivity(intent)
        }
    }
}