package com.example.bp_frontend.loginLogic

import android.content.Context
import android.content.SharedPreferences
import com.example.bp_frontend.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val TOKEN = "user_token"
    }

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN, null)
    }
}