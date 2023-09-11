package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName

data class RatingModel(
    @SerializedName("average")
    val average: Double?
)