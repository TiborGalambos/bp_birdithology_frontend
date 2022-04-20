package com.example.bp_frontend.loginLogic

import android.content.Context
import android.content.SharedPreferences
import com.example.bp_frontend.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val TOKEN = "user_token"
        const val USERNAME = "username"
        const val USER_TYPE = "classic"
    }

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun saveUsername(username: String) {
        val editor = prefs.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun saveUserType(username: String) {
        val editor = prefs.edit()
        editor.putString(USER_TYPE, username)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN, null)
    }

    fun getUsername(): String? {
        return prefs.getString(USERNAME, null)
    }

    fun getUserType(): String? {
        return prefs.getString(USER_TYPE, null)
    }

}