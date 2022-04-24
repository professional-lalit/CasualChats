package com.casualchats.app.home.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.casualchats.app.models.User

data class ProfileData(
    val firstName: MutableState<String>,
    val lastName: MutableState<String>,
    val phoneNumber: MutableState<String>,
    val userImageUri: MutableState<String>,
    val isLoading: MutableState<Boolean>
) {
    var isDataUpdated = mutableStateOf(false)
    var isImageUpdated = mutableStateOf(false)
}
