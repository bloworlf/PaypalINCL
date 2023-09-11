package io.drdroid.paypalincl.data.repository

import com.google.firebase.auth.FirebaseUser
import io.drdroid.paypalincl.data.model.auth.AccessTokenModel
import io.drdroid.paypalincl.data.model.tv_show.EpisodeModel
import io.drdroid.paypalincl.data.model.tv_show.SearchItemModel
import io.drdroid.paypalincl.data.model.tv_show.SeasonModel
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import retrofit2.Response

interface Repository {

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String
    ): Response<AccessTokenModel>

    val currentUser: FirebaseUser?

    suspend fun createEmailPasswordAccount(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): FirebaseUser?

    suspend fun loginEmailPassword(
        email: String,
        password: String
    ): FirebaseUser?

    suspend fun googleLogin(idToken: String?): FirebaseUser?

    suspend fun facebookLogin(token: String): FirebaseUser?

    suspend fun signOut()

    suspend fun search(
        q: String = ""
    ): Response<ArrayList<SearchItemModel>>

//    suspend fun schedule(): Response<ArrayList<SearchItemModel>>

    suspend fun getSeasons(
        id: Int
    ): Response<ArrayList<SeasonModel>>

    suspend fun getSeasonEpisodes(
        id: Int
    ): Response<ArrayList<EpisodeModel>>

    suspend fun getShows(
        page: Int = 1
    ): Response<ArrayList<ShowModel>>
}