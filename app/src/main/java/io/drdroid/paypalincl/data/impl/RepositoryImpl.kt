package io.drdroid.paypalincl.data.impl

import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import io.drdroid.paypalincl.data.model.auth.AccessTokenModel
import io.drdroid.paypalincl.data.model.tv_show.EpisodeModel
import io.drdroid.paypalincl.data.model.tv_show.SearchItemModel
import io.drdroid.paypalincl.data.model.tv_show.SeasonModel
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.data.network.PaypalApiCall
import io.drdroid.paypalincl.data.network.TvShowCall
import io.drdroid.paypalincl.data.repository.Repository
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val paypalApiCall: PaypalApiCall,
    private val tvShowCall: TvShowCall,
) : Repository {
    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String
    ): Response<AccessTokenModel> = paypalApiCall.getAccessToken(clientId = clientId, clientSecret = clientSecret)

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun createEmailPasswordAccount(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): FirebaseUser? {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await().user?.let {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("$firstName $lastName")
//                .setPhotoUri(Uri.parse(""))
                .build()
            it.updateProfile(profileUpdates).await()
            return it
        }
        return null
    }

    override suspend fun loginEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await().user?.let {
            return it
        }
        return null
    }

    override suspend fun googleLogin(idToken: String?): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).await().user?.let {
            return it
        }
        return null
    }

    override suspend fun facebookLogin(token: String): FirebaseUser? {
        val credential = FacebookAuthProvider.getCredential(token)
        firebaseAuth.signInWithCredential(credential).await().user?.let {
            return it
        }
        return null
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        LoginManager.getInstance().logOut()
    }

    override suspend fun search(q: String): Response<ArrayList<SearchItemModel>> = tvShowCall.search(q = q)

//    override suspend fun schedule(): Response<ArrayList<SearchItemModel>> = apiCall

    override suspend fun getSeasons(id: Int): Response<ArrayList<SeasonModel>> = tvShowCall.getSeasons(id = id)

    override suspend fun getSeasonEpisodes(id: Int): Response<ArrayList<EpisodeModel>> = tvShowCall.getSeasonEpisodes(id = id)

    override suspend fun getShows(page: Int): Response<ArrayList<ShowModel>> = tvShowCall.getShows(page = page)
}