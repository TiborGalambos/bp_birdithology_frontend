package com.example.bp_frontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unconfirmed_item_content)
        onClickListeners()

        val bird_count = intent.getIntExtra("bird_count", 0)
        val bird_name = intent.getStringExtra("bird_name")
        val photo = intent.getStringExtra("photo")
        val location = intent.getStringExtra("obs_location")
        val idOfItem:Int = intent.getIntExtra("id", 0)

        val box_bird_name = findViewById<TextView>(R.id.bird_name)
        val box_bird_number = findViewById<TextView>(R.id.bird_number)
        val box_location = findViewById<TextView>(R.id.obs_location)
        val photo_box = findViewById<ImageView>(R.id.photo_content)

        Picasso.with(this)
            .load("https://birdithology.azurewebsites.net/".plus(photo))
            .into(photo_box)

        box_bird_name.text = bird_name?.replace("(^\\(|\\)$)", "")
        box_bird_number.text = bird_count.toString()
        box_location.text = location?.replace("(^\\(|\\)$)", "")


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
                        finish()
                        Toast.makeText(applicationContext, "Úspešne uložené", Toast.LENGTH_SHORT)
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



    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun onClickListeners() {
        val left_top_text = findViewById(R.id.left_top_text) as TextView

        left_top_text.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
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