package com.example.bp_frontend.dataItems

import com.google.gson.annotations.SerializedName

data class GraphDataItem(
    @SerializedName("year")
    val year: Year
)