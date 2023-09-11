package io.drdroid.paypalincl.data.network.response

data class Response<T>(
    val status: ResponseStatus,
    val message: String? = null,
    val body: T? = null
)

enum class ResponseStatus {
    SUCCESS,
    FAILURE,
    ERROR
}
