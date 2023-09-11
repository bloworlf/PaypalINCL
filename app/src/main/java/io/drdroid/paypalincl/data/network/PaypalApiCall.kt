package io.drdroid.paypalincl.data.network

import io.drdroid.paypalincl.data.model.auth.AccessTokenModel
import io.drdroid.paypalincl.utils.Common
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PaypalApiCall {

    @FormUrlEncoded
    @POST(Common.PAYPAL_API.AUTH_TOKEN_ENDPOINT)
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<AccessTokenModel>

}