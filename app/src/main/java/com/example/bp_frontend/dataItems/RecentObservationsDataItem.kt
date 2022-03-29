package com.example.bp_frontend.dataItems

import com.google.gson.annotations.SerializedName

data class RecentObservationsDataItem(
    @SerializedName("obs")
    var obs: List<NormalObservationDataItem>
)