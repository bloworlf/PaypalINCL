package io.drdroid.paypalincl.data.model.tv_show


import com.google.gson.annotations.SerializedName

data class EmbeddedModel(
    @SerializedName("episodes")
    val episodes: List<EpisodeModel>?,
    @SerializedName("seasons")
    val seasons: List<SeasonModel>?
)