package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName

data class ExternalsModel(
    @SerializedName("imdb")
    val imdb: String?,
    @SerializedName("thetvdb")
    val thetvdb: Int?,
    @SerializedName("tvrage")
    val tvrage: Int?
)