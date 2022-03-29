package com.example.bp_frontend

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.backendEndpoints.RegisterResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {

    private lateinit var apiClient: BackendApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        buttonListeners()

        val text_signup = findViewById(R.id.sign_up) as TextView
        val button_signup = findViewById(R.id.button_next_step) as View

        button_signup.setOnClickListener {

            setClickedProperty(button_signup, text_signup)

            val name_bar = findViewById(R.id.name_bar) as TextInputEditText
            val password_bar = findViewById(R.id.password_bar) as TextInputEditText


            val username = name_bar.text.toString()
            val password = password_bar.text.toString()

            validate(username, name_bar, button_signup, password, password_bar, text_signup)


            apiClient = BackendApiClient()

            apiClient.getApiService(this).register(username, password).enqueue(object : Callback<RegisterResponse?> {
                override fun onResponse(
                    call: Call<RegisterResponse?>,
                    response: Response<RegisterResponse?>
                ) {
                    if(response.code() == 200){
                        Toast.makeText(applicationContext,"Your registration was successful.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    if(response.code() == 400){
                        Toast.makeText(applicationContext,"We are sorry, this username is taken.", Toast.LENGTH_SHORT).show()
                        setOriginalProperty(button_signup, text_signup)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext,"Something went wrong, try again later.", Toast.LENGTH_SHORT).show()
                    setOriginalProperty(button_signup, text_signup)
                }
            })
        }

    }

    private fun buttonListeners() {
        val login_text_button = findViewById(R.id.right_top_text) as TextView

        login_text_button.setOnClickListener {

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        val cancel_button = findViewById(R.id.left_top_text) as TextView

        cancel_button.setOnClickListener {
            val intent = Intent(applicationContext, WelcomeActivity::class.java)

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
        }
    }

    private fun validate(
        username: String,
        name_bar: TextInputEditText,
        button_signup: View,
        password: String,
        password_bar: TextInputEditText,
        text_signup: TextView
    ) {
        if (username.isEmpty()) {
            name_bar.error = "Username required"
            name_bar.requestFocus()
            setOriginalProperty(button_signup, text_signup)
        }

        if (password.isEmpty()) {
            password_bar.error = "Password required"
            password_bar.requestFocus()
            setOriginalProperty(button_signup, text_signup)
        }
    }

    private fun setClickedProperty(button_signup: View, text_signup: TextView) {
        button_signup.setBackgroundResource(R.drawable.button_prim_dark) // set dark color
        button_signup.isEnabled = false
        button_signup.isClickable = false
        text_signup.text = "Loading"
    }

    private fun setOriginalProperty(button_signup: View, text_signup: TextView) {
        button_signup.setBackgroundResource(R.drawable.button_prim) // set orginal color
        button_signup.isEnabled = true
        button_signup.isClickable = true
        text_signup.text = "Sign Up"
    }

}

