package com.example.navigationcomponentbasics.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.navigationcomponentbasics.api.UserApi
import com.example.navigationcomponentbasics.models.UserRequest
import com.example.navigationcomponentbasics.models.UserResponse
import com.example.navigationcomponentbasics.utils.Constants
import com.example.navigationcomponentbasics.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val mUserResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = mUserResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        mUserResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signUp(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        mUserResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signIn(userRequest)
        handleResponse(response)
    }

    private inline fun handleResponse(response: Response<UserResponse>) {
        Log.d(Constants.TAG, "response: ${response.body().toString()}")
        if (response.isSuccessful && response.body() != null) {
            mUserResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            mUserResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        } else {
            mUserResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}