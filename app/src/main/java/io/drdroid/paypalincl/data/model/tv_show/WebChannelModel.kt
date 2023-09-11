package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName
import io.drdroid.paypalincl.data.model.tv_show.CountryModel

data class WebChannelModel(
    @SerializedName("country")
    val country: CountryModel?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("officialSite")
    val officialSite: String?
)