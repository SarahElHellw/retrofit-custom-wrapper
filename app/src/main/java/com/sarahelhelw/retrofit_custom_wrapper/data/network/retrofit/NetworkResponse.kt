package com.sarahelhelw.retrofit_custom_wrapper.data.network.retrofit

import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val data: T) : NetworkResponse<T, Nothing>()

    /**
     * For mapping non 2XX responses
     * @param errorBody error body
     * @param code response status code
     */
    data class ApiError<U : Any>(val errorBody: U, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error/No internet connection
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, request creation error/json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}