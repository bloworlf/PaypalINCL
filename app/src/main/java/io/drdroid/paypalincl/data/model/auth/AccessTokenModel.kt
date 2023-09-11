package io.drdroid.paypalincl.data.model.auth


import com.google.gson.annotations.SerializedName

data class AccessTokenModel(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("app_id")
    val appId: String?,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("nonce")
    val nonce: String?,
    @SerializedName("scope")
    val scope: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    private val createdAt: Long = System.currentTimeMillis()
) {
//    private val createdAt = System.currentTimeMillis()

    fun hasExpired(): Boolean {
        return System.currentTimeMillis() < (createdAt + (expiresIn * 1000))
    }

    override fun toString(): String {
        return this.accessToken
    }
}