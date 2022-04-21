package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.bp_frontend.ListAdapter.CommentListAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.Comment
import com.example.bp_frontend.dataItems.DeleteConfirm
import com.example.bp_frontend.dataItems.UpdateConfirm
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalItemContent : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item_content)

        onClickListeners()

        val com_author: Array<String> = intent.getStringArrayExtra("com_author")!!
        val comment: Array<String> = intent.getStringArrayExtra("comment")!!

        val bird_count = intent.getIntExtra("bird_count", 0)
        val bird_name = intent.getStringExtra("bird_name")
        val photo = intent.getStringExtra("photo")
        val location = intent.getStringExtra("obs_location")
        val idOfItem:Int = intent.getIntExtra("id", 0)


        val box_bird_name = findViewById<TextView>(R.id.bird_name)
        val box_bird_number = findViewById<TextView>(R.id.bird_number)
        val box_location = findViewById<TextView>(R.id.obs_location)
        val photo_box = findViewById<ImageView>(R.id.photo_content)

        val delete_button = findViewById<View>(R.id.delete_button)


        Picasso.with(this)
            .load("https://birdithology.azurewebsites.net/".plus(photo))
            .into(photo_box)


        box_bird_name.text = bird_name?.replace("(^\\(|\\)$)", "")
        box_bird_number.text = bird_count.toString()
        box_location.text = location?.replace("(^\\(|\\)$)", "")


        var com_adapter = CommentListAdapter(
            this,
            comment,
            com_author
        )

        val comments_id = findViewById<ListView>(R.id.comments_id)
        comments_id.adapter = com_adapter

        val comment_button = findViewById(R.id.play_button) as View

        val comment_bar = findViewById<TextInputEditText>(R.id.comment_bar1)

        val comment_button_text = findViewById<TextView>(R.id.textView2)

        comment_button.setOnClickListener {

            if (comment_bar.text!!.isEmpty()) {
                comment_bar.error = "This field is required"
                comment_bar.requestFocus()
                return@setOnClickListener
            }

            setClickedProperty(comment_button, comment_button_text)

            apiClient = BackendApiClient()
            sessionManager = SessionManager(this)

            val user:String = sessionManager.getUsername().toString()

            comment_button.isClickable = false

            apiClient.getApiService(this)
                .addNewComment(
                    token = "Token ${sessionManager.getToken()}",
                    comment = comment_bar.text.toString(),
                    observation_id = idOfItem.toInt()
                ).enqueue(object : Callback<Comment?> {
                    override fun onResponse(call: Call<Comment?>, response: Response<Comment?>) {
                        if(response.code() == 200)
                        {
                            Toast.makeText(applicationContext, "Komentár pridaný", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, MyCollectionActivity::class.java)

                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()

                        }
                        if(response.code() != 200){

                            setOriginalProperty(comment_button, comment_button_text)
                            Toast.makeText(applicationContext, "${response.code()}", Toast.LENGTH_SHORT).show()
                            Log.d("my_debug", "token ${sessionManager.getToken()} \n " +
                                    "comment = ${comment_bar.text.toString()},\n" +
                                    "id of item = ${idOfItem.toInt()},\n" +
                                    "$response")

                        }
                    }

                    override fun onFailure(call: Call<Comment?>, t: Throwable) {
                        Toast.makeText(applicationContext, "FAIL", Toast.LENGTH_SHORT).show()
                        setOriginalProperty(comment_button, comment_button_text)
                    }
                })

        }


        delete_button.setOnClickListener {

            apiClient = BackendApiClient()
            sessionManager = SessionManager(this)

            apiClient.getApiService(this)
                .deleteMyObservation(
                    token = "Token ${sessionManager.getToken()}",
                    obs_number = idOfItem.toInt()
                ).enqueue(object : Callback<DeleteConfirm?> {
                    override fun onResponse(
                        call: Call<DeleteConfirm?>,
                        response: Response<DeleteConfirm?>
                    ) {
                        val intent2 = Intent(this@PersonalItemContent, MyCollectionActivity::class.java)

                        if(response.code() == 201 && response.body()?.deleted == true)
                        {
                            Toast.makeText(applicationContext, "Pozorovanie úspešne vymazané.", Toast.LENGTH_SHORT).show()
                            startActivity(intent2)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
                            finish()
                        }

                        if(response.code() == 401 && response.body()?.deleted == false)
                        {
                            Toast.makeText(applicationContext, "Na vymazanie nemáte oprávnenie.", Toast.LENGTH_SHORT).show()
                            startActivity(intent2)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
                            finish()
                        }
                        if(response.code() == 404 && response.body()?.deleted == false)
                        {
                            Toast.makeText(applicationContext, "Pozorovanie nenájdené.", Toast.LENGTH_SHORT).show()
                            startActivity(intent2)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
                            finish()
                        }


                    }

                    override fun onFailure(call: Call<DeleteConfirm?>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })


        }

    }

    private fun onClickListeners() {
        val left_top_text = findViewById(R.id.left_top_text) as TextView

        left_top_text.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
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