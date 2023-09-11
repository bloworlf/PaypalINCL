package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName

data class SearchItemModel(
    @SerializedName("score")
    val score: Double,
    @SerializedName("show")
    val show: ShowModel
)