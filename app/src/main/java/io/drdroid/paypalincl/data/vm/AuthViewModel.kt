package io.drdroid.paypalincl.data.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.drdroid.paypalincl.data.manager.AccessTokenManager
import io.drdroid.paypalincl.data.model.auth.AccessTokenModel
import io.drdroid.paypalincl.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.security.KeyStore
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val accessTokenLiveData: MutableLiveData<AccessTokenModel> by lazy {
        MutableLiveData<AccessTokenModel>()
    }

    fun getAccessToken(clientId: String, clientSecret: String) {
        val tokenManager = AccessTokenManager()
        val accessTokenModel = tokenManager.retrieveAccessToken()

        accessTokenModel?.let {
            if (it.hasExpired()) {
                //refresh the token
            } else {
                //return the accessToken
                accessTokenLiveData.value = it
            }
        } ?: run {
            //get a new access token
            fetchAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                accessTokenManager = tokenManager
            )
        }
    }

    private fun fetchAccessToken(
        clientId: String,
        clientSecret: String,
        accessTokenManager: AccessTokenManager
    ) {
        viewModelScope.launch {
            val value = repository.getAccessToken(clientId = clientId, clientSecret = clientSecret)
            if (value.isSuccessful) {
                accessTokenLiveData.postValue(value.body())

                accessTokenManager.saveAccessToken(value.body())
            }
        }
    }
}