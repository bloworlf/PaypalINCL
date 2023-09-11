package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName

data class LinksModel(
    @SerializedName("previousepisode")
    val previousepisode: PreviousepisodeModel?,
    @SerializedName("self")
    val self: SelfModel?
)