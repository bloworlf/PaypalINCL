package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName

data class CountryModel(
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("timezone")
    val timezone: String?
)