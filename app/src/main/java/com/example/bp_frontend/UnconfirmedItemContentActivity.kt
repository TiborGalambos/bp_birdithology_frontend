package com.example.bp_frontend

import android.content.Context
import android.content.Intent
import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.EbirdDataItem
import com.example.bp_frontend.dataItems.UpdateConfirm
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class UnconfirmedItemContentActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient

    private lateinit var new_bird_name: String

    val items: MutableList<String> = ArrayList()
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unconfirmed_item_content)
        onClickListeners()

        val bird_count = intent.getIntExtra("bird_count", 0)
        val bird_name = intent.getStringExtra("bird_name")
        val photo = intent.getStringExtra("photo")
        val location = intent.getStringExtra("obs_location")
        val idOfItem:Int = intent.getIntExtra("id", 0)

        val bird_recording = intent.getStringExtra("bird_recording")

        val obs_is_simple = intent.getBooleanExtra("obs_is_simple", false)

        val obs_size = findViewById<TextView>(R.id.obs_size)
        val obs_color = findViewById<TextView>(R.id.obs_color)
        val obs_beak = findViewById<TextView>(R.id.obs_beak)

        val simple_desc = findViewById<LinearLayout>(R.id.simple_descriptions)

        val play_button = findViewById<View>(R.id.play_button)

        if(obs_is_simple)
        {
            val bird_color = intent.getStringExtra("bird_color")
            val bird_size = intent.getStringExtra("bird_size")
            val bird_beak = intent.getStringExtra("bird_beak")

            obs_size.text = bird_size?.replace("(^\\(|\\)$)", "")?.replace("\"", "")
            obs_color.text = bird_color?.replace("(^\\(|\\)$)", "")?.replace("\"", "")
            obs_beak.text = bird_beak?.replace("(^\\(|\\)$)", "")?.replace("\"", "")
        }
        else simple_desc.isVisible = false



        val box_bird_name = findViewById<TextView>(R.id.bird_name)
        val box_bird_number = findViewById<TextView>(R.id.bird_number)
        val box_location = findViewById<TextView>(R.id.obs_location)
        val photo_box = findViewById<ImageView>(R.id.photo_content)
        val play_icon = findViewById<ImageView>(R.id.play_icon)

        if(!bird_recording.isNullOrEmpty())
        {
            mediaPlayer = MediaPlayer.create(this, ("https://birdithology.azurewebsites.net/".plus(bird_recording)).toUri())
            mediaPlayer?.setOnPreparedListener{
                Toast.makeText(applicationContext, "Nahrávka načítaná", Toast.LENGTH_SHORT)
            }


            play_button.setOnClickListener {
                mediaPlayer?.start()
                play_icon.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
            }

            mediaPlayer?.setOnCompletionListener {
                play_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }

        }
        else
        {
            val recording = findViewById<LinearLayout>(R.id.recording)
            recording.isVisible =false
        }


        Picasso.with(this)
            .load("https://birdithology.azurewebsites.net/".plus(photo))
            .into(photo_box)

        box_bird_name.text = bird_name?.replace("(^\\(|\\)$)", "")?.replace("\"", "")
        box_bird_number.text = bird_count.toString()
        box_location.text = location?.replace("(^\\(|\\)$)", "")?.replace("\"", "")


        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.dropdown_menu)
        val loading = findViewById<TextView>(R.id.loading)

        loading.isVisible = true
        autoCompleteTextView.isVisible = false

        loadData(autoCompleteTextView, loading)

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }




        val confirm_button = findViewById<View>(R.id.confirm_button)

        confirm_button.setOnClickListener {
            new_bird_name = autoCompleteTextView.text.toString().replace("\"", "")

            if (new_bird_name.isNullOrEmpty())
                new_bird_name = bird_name.toString().replace("\"", "")

            setClickedProperty(confirm_button, findViewById(R.id.textView3))

            apiClient = BackendApiClient()
            sessionManager = SessionManager(this)


            apiClient.getApiService(this@UnconfirmedItemContentActivity).updateObservation(
                obs_number= idOfItem,
                bird_name = new_bird_name,
                token = "Token ${sessionManager.getToken()}").enqueue(object : Callback<UpdateConfirm?> {
                override fun onResponse(
                    call: Call<UpdateConfirm?>,
                    response: Response<UpdateConfirm?>
                ) {
                    if (response.code() == 200){
                        val intent = Intent(applicationContext, AdminConfirmActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
                        finish()
                        Toast.makeText(applicationContext, "Úspešne potvrené", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<UpdateConfirm?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        }

        val decline = findViewById(R.id.decline_button) as View

        decline.setOnClickListener {

            apiClient = BackendApiClient()
            sessionManager = SessionManager(this)

            apiClient.getApiService(this@UnconfirmedItemContentActivity).deleteObservation(
                obs_number= idOfItem,
                token = "Token ${sessionManager.getToken()}"
            ).enqueue(object : Callback<UpdateConfirm?> {
                override fun onResponse(
                    call: Call<UpdateConfirm?>,
                    response: Response<UpdateConfirm?>
                ) {
                    if (response.code() == 201)
                    {
                        val intent = Intent(applicationContext, AdminConfirmActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
                        finish()

                        Toast.makeText(applicationContext, "Úspešne vymazané", Toast.LENGTH_SHORT).show()


                    }
                }

                override fun onFailure(call: Call<UpdateConfirm?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        }




    }

    private fun loadData(dropdown_menu: AutoCompleteTextView, loading: TextView) {
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

                if(response.code() == 200){
                    for (myData in responseBody) {
                        items.add(myData.comName)
                    }
                    dropdown_menu.isVisible = true
                    loading.isVisible = false

                    val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.dropdown_menu)
                    val adapter =
                        ArrayAdapter(this@UnconfirmedItemContentActivity, android.R.layout.simple_list_item_1, items)
                    autoCompleteTextView.setAdapter(adapter)
                }

            }
            override fun onFailure(call: Call<List<EbirdDataItem>?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }


    private fun onClickListeners() {
        val left_top_text = findViewById(R.id.left_top_text) as TextView

        left_top_text.setOnClickListener {
            val intent = Intent(applicationContext, AdminConfirmActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
            finish()
        }




    }

    private fun setClickedProperty(button_submit: View, login_text: TextView) {
        button_submit.setBackgroundResource(R.drawable.add_photo_button_dark) // set dark color
        button_submit.isEnabled = false
        button_submit.isClickable = false
        login_text.text = "Počkajte.."
    }

    private fun setOriginalProperty(button_submit: View, login_text: TextView) {
        button_submit.setBackgroundResource(R.drawable.add_photo_button) // set original color
        button_submit.isEnabled = true
        button_submit.isClickable = true
        login_text.text = "Komentovať"
    }

}