package io.drdroid.paypalincl.data.network

import com.google.firebase.auth.FirebaseUser

interface AccountCall {

    fun getUser(): FirebaseUser

    suspend fun loginEmailPassword(email: String, password: String): FirebaseUser

    suspend fun createAccount(email: String, password: String): FirebaseUser
}