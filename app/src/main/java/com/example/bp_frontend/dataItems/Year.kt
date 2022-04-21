package com.example.bp_frontend.dataItems

import com.google.gson.annotations.SerializedName

data class Year(

    @SerializedName("jan")
    val jan: Int,

    @SerializedName("feb")
    val feb: Int,

    @SerializedName("mar")
    val mar: Int,

    @SerializedName("apr")
    val apr: Int,

    @SerializedName("maj")
    val maj: Int,

    @SerializedName("jun")
    val jun: Int,

    @SerializedName("jul")
    val jul: Int,

    @SerializedName("aug")
    val aug: Int,

    @SerializedName("sep")
    val sep: Int,

    @SerializedName("oct")
    val oct: Int,

    @SerializedName("nov")
    val nov: Int,

    @SerializedName("dec")
    val dec: Int,

)