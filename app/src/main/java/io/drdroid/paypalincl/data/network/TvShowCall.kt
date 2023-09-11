package io.drdroid.paypalincl.data.network

import io.drdroid.paypalincl.data.model.tv_show.EpisodeModel
import io.drdroid.paypalincl.data.model.tv_show.SearchItemModel
import io.drdroid.paypalincl.data.model.tv_show.SeasonModel
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.utils.Common
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowCall {

    @GET(Common.TV_SHOW_API.SEARCH)
    suspend fun search(
        @Query("q") q: String = ""
    ): Response<ArrayList<SearchItemModel>>

    @GET(Common.TV_SHOW_API.SCHEDULE)
    suspend fun schedule(): Response<ArrayList<SearchItemModel>>

    @GET(Common.TV_SHOW_API.SEASONS)
    suspend fun getSeasons(
        @Path("id") id: Int
    ): Response<ArrayList<SeasonModel>>

    @GET(Common.TV_SHOW_API.EPISODES)
    suspend fun getSeasonEpisodes(
        @Path("id") id: Int
    ): Response<ArrayList<EpisodeModel>>

    @GET(Common.TV_SHOW_API.SHOWS)
    suspend fun getShows(
        @Query("page") page: Int = 1
    ): Response<ArrayList<ShowModel>>
}