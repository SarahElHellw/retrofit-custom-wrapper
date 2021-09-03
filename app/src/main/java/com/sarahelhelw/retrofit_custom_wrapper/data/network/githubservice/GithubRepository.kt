package com.sarahelhelw.retrofit_custom_wrapper.data.network.githubservice

import com.sarahelhelw.retrofit_custom_wrapper.data.network.retrofit.NetworkResponse
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApiService: IGithubApiService) {

    suspend fun getUserProfile(profile: String): NetworkResponse<GithubUser, Error> {
        return githubApiService.getUserData(profile)
    }
}