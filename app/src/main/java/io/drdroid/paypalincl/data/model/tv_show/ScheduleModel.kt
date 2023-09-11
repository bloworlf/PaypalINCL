package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName

data class ScheduleModel(
    @SerializedName("days")
    val days: List<String?>?,
    @SerializedName("time")
    val time: String?
)