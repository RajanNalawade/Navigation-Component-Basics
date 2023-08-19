package com.example.navigationcomponentbasics

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationcomponentbasics.models.UserRequest
import com.example.navigationcomponentbasics.models.UserResponse
import com.example.navigationcomponentbasics.repository.UserRepository
import com.example.navigationcomponentbasics.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateUserCredentials(
        userName: String,
        email: String,
        password: String,
        isLogin: Boolean
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (!isLogin && TextUtils.isEmpty(userName)) {
            result = Pair(false, "Please provide User Name")
        } else if (TextUtils.isEmpty(email)) {
            result = Pair(false, "Please provide User Email Address")
        } else if (TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide Password")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Please provide valid email address")
        } else if (password.length != 6) {
            result = Pair(false, "Password length should be 6")
        }
        return result
    }

}