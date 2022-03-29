package com.example.bp_frontend.backendEndpoints

import com.google.gson.annotations.SerializedName

data class RegisterResponse (

    @SerializedName("user")
    var user: String,

    @SerializedName("token")
    var token: String,

)