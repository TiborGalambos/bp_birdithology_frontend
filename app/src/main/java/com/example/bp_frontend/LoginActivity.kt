package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.backendEndpoints.LoginResponse
import com.example.bp_frontend.loginLogic.SessionManager
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var apiClient: BackendApiClient
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonListeners()

        val button_login = findViewById(R.id.button_login) as RelativeLayout
        val login_text = findViewById(R.id.login_text) as TextView

        button_login.setOnClickListener {

            setClickedProperty(button_login, login_text)

            val name_bar = findViewById(R.id.name_bar) as TextInputEditText
            val password_bar = findViewById(R.id.password_bar) as TextInputEditText

            val username = name_bar.text.toString()
            val password = password_bar.text.toString()

            validate(username, name_bar, button_login, password, password_bar, login_text)

            apiClient = BackendApiClient()
            sessionManager = SessionManager(this)

            apiClient.getApiService(this).login(username,password).enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    if(response.code() == 200){
                        Toast.makeText(applicationContext,"Login successful.", Toast.LENGTH_SHORT).show()
                        setOriginalProperty(button_login, login_text)

                        val loginResponse = response.body()

                        if (loginResponse != null){
                            sessionManager.saveToken(loginResponse.token)
                            sessionManager.saveUsername(username)
                        }

                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }

                    if(response.code() == 400){
                        Toast.makeText(applicationContext,"Nedokážem sa prihlásiť.", Toast.LENGTH_SHORT).show()
                        password_bar.setText("")
                        password_bar.error = "Heslo je povinné"
                        setOriginalProperty(button_login, login_text)
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext,"Niečo sa pokazilo.", Toast.LENGTH_SHORT).show()
                    setOriginalProperty(button_login, login_text)
                }
            })

        }
    }

    private fun validate(
        username: String,
        name_bar: TextInputEditText,
        button_login: RelativeLayout,
        password: String,
        password_bar: TextInputEditText,
        login_text: TextView
    ) {
        if (username.isEmpty()) {
            name_bar.error = "Meno je povinné"
            name_bar.requestFocus()
            setOriginalProperty(button_login, login_text)
        }

        if (password.isEmpty()) {
            password_bar.error = "Heslo je povinné"
            password_bar.requestFocus()
            setOriginalProperty(button_login, login_text)
        }
    }

    private fun setOriginalProperty(button_login: RelativeLayout, login_text: TextView) {
        button_login.setBackgroundResource(R.drawable.button_prim) // set original color
        button_login.isEnabled = true
        button_login.isClickable = true
        login_text.text = "Prihlásiť sa"
    }

    private fun setClickedProperty(button_login: RelativeLayout, login_text: TextView) {
        button_login.setBackgroundResource(R.drawable.button_prim_dark) // set dark color
        button_login.isEnabled = false
        button_login.isClickable = false
        login_text.text = "Načítavam"
    }


    private fun buttonListeners() {
        val signup_text_button = findViewById(R.id.right_top_text) as TextView

        signup_text_button.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        val cancel_button = findViewById(R.id.left_top_text) as TextView

        cancel_button.setOnClickListener {
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // backwards
        }
    }
}