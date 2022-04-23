package com.casualchats.app.otp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casualchats.app.common.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtpVM: ViewModel() {

    val isLoadingState = mutableStateOf(false)
    val otp = mutableStateOf("")
    val navigateTo = MutableLiveData<Screen>()

    fun submit() {
        viewModelScope.launch {
            isLoadingState.value = true
            delay(2000L)
            isLoadingState.value = false
            navigateTo.postValue(Screen.Login())
        }
    }
}