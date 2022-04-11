package com.example.bp_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bp_frontend.backendEndpoints.BackendApiClient
import com.example.bp_frontend.dataItems.WhoAmIDataItem
import com.example.bp_frontend.loginLogic.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private lateinit var apiClient: BackendApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        apiClient = BackendApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService(this@LogoActivity).whoAmI(token = "Token ${sessionManager.getToken()}")
            .enqueue(object : Callback<WhoAmIDataItem?> {
                override fun onResponse(
                    call: Call<WhoAmIDataItem?>,
                    response: Response<WhoAmIDataItem?>
                ) {
                    if (response.code() == 200)
                    {
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    if (response.code() == 401)
                    {
                        val intent = Intent(applicationContext, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<WhoAmIDataItem?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


    }
}