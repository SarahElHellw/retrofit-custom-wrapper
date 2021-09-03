package com.sarahelhelw.retrofit_custom_wrapper.di

import com.sarahelhelw.retrofit_custom_wrapper.data.network.NetworkConstants
import com.sarahelhelw.retrofit_custom_wrapper.data.network.githubservice.IGithubApiService
import com.sarahelhelw.retrofit_custom_wrapper.data.network.retrofit.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
class GithubModule {

    @Provides
    @Singleton
    fun provideGithubService( retrofit: Retrofit): IGithubApiService {
        return retrofit.create(IGithubApiService::class.java)
    }
}