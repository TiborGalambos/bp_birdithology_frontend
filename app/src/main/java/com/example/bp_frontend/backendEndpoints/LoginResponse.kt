package com.example.bp_frontend.backendEndpoints

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("user")
    var user: String,

    @SerializedName("token")
    var token: String,
)
