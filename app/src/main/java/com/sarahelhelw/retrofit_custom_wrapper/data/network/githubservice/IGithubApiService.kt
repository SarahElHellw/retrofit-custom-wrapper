package com.sarahelhelw.retrofit_custom_wrapper.data.network.githubservice

import com.sarahelhelw.retrofit_custom_wrapper.data.network.retrofit.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface IGithubApiService{

    @GET("/users/{profile}")
    suspend fun getUserData(@Path("profile") profile: String) : NetworkResponse<GithubUser, Error>
}

