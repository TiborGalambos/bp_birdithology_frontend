package com.example.bp_frontend.dataItems

import com.google.gson.annotations.SerializedName

data class ObservationList(
    @SerializedName("obs")
    var obs: List<ObservationDataItem>
)