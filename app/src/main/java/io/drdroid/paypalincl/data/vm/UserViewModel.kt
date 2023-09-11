package io.drdroid.paypalincl.data.vm

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.drdroid.paypalincl.data.network.response.Response
import io.drdroid.paypalincl.data.network.response.ResponseStatus
import io.drdroid.paypalincl.data.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val userLiveData: MutableLiveData<FirebaseUser?> by lazy {
        MutableLiveData<FirebaseUser?>()
    }

    val currentUser = repository.currentUser

    fun getUser(): FirebaseUser? = userLiveData.value

    fun loginEmailPassword(
        email: String,
        password: String,
        onLogin: (Response<FirebaseUser>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val value = repository.loginEmailPassword(email, password)
                userLiveData.postValue(value)
                value?.let {
                    onLogin.invoke(Response(ResponseStatus.SUCCESS))
                } ?: run {
                    onLogin.invoke(Response(ResponseStatus.FAILURE, "Couldn't connect"))
                }
            } catch (e: Exception) {
                onLogin.invoke(Response(ResponseStatus.ERROR, e.message))
            }
        }
    }

    fun createAccount(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        profilePicture: Uri? = null,
        onLogin: (Response<FirebaseUser>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val value = repository.createEmailPasswordAccount(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName
                )
                userLiveData.postValue(value)
                value?.let {
                    onLogin.invoke(Response(ResponseStatus.SUCCESS))
                } ?: run {
                    onLogin.invoke(Response(ResponseStatus.FAILURE, "Couldn't connect"))
                }
            } catch (e: Exception) {
                onLogin.invoke(Response(ResponseStatus.ERROR, e.message))
            }
        }
    }

    fun googleLogin(token: String?, onLogin: (Response<FirebaseUser>) -> Unit) {
        viewModelScope.launch {
            try {
                val value = repository.googleLogin(idToken = token)
                userLiveData.postValue(value)
                value?.let {
                    onLogin.invoke(Response(ResponseStatus.SUCCESS))
                } ?: run {
                    onLogin.invoke(Response(ResponseStatus.FAILURE, "Couldn't connect"))
                }
            } catch (e: Exception) {
                onLogin.invoke(Response(ResponseStatus.ERROR, e.message))
            }
        }
    }

    fun signOut(onSignOut: (Response<FirebaseUser>) -> Unit) {
        viewModelScope.launch {
            try {
                repository.signOut()
                onSignOut.invoke(Response(ResponseStatus.SUCCESS))
            } catch (e: Exception) {
                onSignOut.invoke(Response(ResponseStatus.ERROR, e.message))
            }
        }
    }
}