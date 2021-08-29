package com.sarahelhelw.retrofit_custom_wrapper.data.retrofit

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

//To create custom Retrofit wrapper class
//we should implement Call interface from retrofit
// and override its implementation
internal class NetworkResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<NetworkResponse<S, E>> {


    /**
     * enqueue function responsible for sending asynchronously
     * and receives a callback for the response in case of success or failure
     */
    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        //here we map Callback<S> to Callback<NetworkResponse<S,E>>
        //S response should be mapped to NetworkResponse<S,E> sealed class
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                val result: NetworkResponse<S, E> =
                    if (response.isSuccessful) {
                        //for status 200..300
                        if (body != null) {
                            NetworkResponse.Success(body)
                        } else {
                            // Response is successful but the body is null
                            NetworkResponse.UnknownError(null)
                        }
                    } else {
                        val errorBody = createErrorBody(error)
                        if (errorBody != null) {
                            NetworkResponse.ApiError(errorBody, code)

                        } else {
                            NetworkResponse.UnknownError(null)
                        }
                    }

                callback.onResponse(this@NetworkResponseCall, Response.success(result))
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> NetworkResponse.NetworkError(throwable)
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }

        })
    }

    /**
     * Create error model from error response
     */
    fun createErrorBody(errorBody: ResponseBody?): E? {
        return when {
            errorBody == null -> null
            errorBody.contentLength() == 0L -> null
            else -> try {
                errorConverter.convert(errorBody)
            } catch (ex: Exception) {
                null
            }
        }
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}