package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.bp_frontend.ListAdapter.CommentListAdapter
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.Comment
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemContent : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_content)


        onClickListeners()

        val com_author: Array<String> = intent.getStringArrayExtra("com_author")!!
        val comment: Array<String> = intent.getStringArrayExtra("comment")!!

        val obs_author = intent.getStringExtra("obs_author")
        val bird_count = intent.getStringExtra("bird_count")
        val bird_name = intent.getStringExtra("bird_name")
        val obs_x_coords = intent.getIntExtra("obs_x_coords",0)
        val obs_y_coords = intent.getIntExtra("obs_y_coords",0)

        val idOfItem:Int = intent.getIntExtra("id", 0)


        val box_obs_author = findViewById<TextView>(R.id.obs_author)
        val box_bird_name = findViewById<TextView>(R.id.bird_name)
        val box_bird_number = findViewById<TextView>(R.id.bird_number)

        box_obs_author.text = obs_author
        box_bird_name.text = bird_name
        box_bird_number.text = bird_count

        var com_adapter = CommentListAdapter(
            this,
            comment,
            com_author
        )

        val comments_id = findViewById<ListView>(R.id.comments_id)
        comments_id.adapter = com_adapter

        val comment_button = findViewById(R.id.comment_button) as View

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
                            val intent = Intent(applicationContext, HomeActivity::class.java)

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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