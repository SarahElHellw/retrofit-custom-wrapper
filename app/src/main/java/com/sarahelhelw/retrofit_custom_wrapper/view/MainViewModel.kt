package com.sarahelhelw.retrofit_custom_wrapper.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarahelhelw.retrofit_custom_wrapper.R
import com.sarahelhelw.retrofit_custom_wrapper.data.network.githubservice.GithubRepository
import com.sarahelhelw.retrofit_custom_wrapper.data.network.githubservice.GithubUser
import com.sarahelhelw.retrofit_custom_wrapper.data.network.retrofit.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val githubRepository: GithubRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<GithubUser>()
    val userProfile : LiveData<GithubUser> = _userProfile

    private val _errorMsg = MutableLiveData<String>()
    val error: LiveData<String> = _errorMsg


    fun getUserProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = githubRepository.getUserProfile("mitchtabian")
            when(result){
                is NetworkResponse.Success -> _userProfile.postValue(result.data)
                is NetworkResponse.ApiError -> _errorMsg.postValue(result.errorBody.message)
                is NetworkResponse.NetworkError ->_errorMsg.postValue("")
            }
        }
    }

}