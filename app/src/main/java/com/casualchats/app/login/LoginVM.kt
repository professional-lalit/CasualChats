package com.casualchats.app.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casualchats.app.common.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginVM : ViewModel() {

    val mobileNumber = mutableStateOf("")
    val isLoadingState = mutableStateOf(false)
    val navigateTo = MutableLiveData<Screen>()

    fun login() {
        viewModelScope.launch {
            isLoadingState.value = true
            delay(2000L)
            isLoadingState.value = false
            navigateTo.postValue(Screen.Otp())
        }
    }

    fun openSignUp() {
        navigateTo.postValue(Screen.SignUp())
    }

}