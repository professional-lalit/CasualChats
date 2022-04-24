package com.casualchats.app.home.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casualchats.app.models.MessageHeader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessagesVM : ViewModel() {

    val messageHeaders = mutableStateOf<List<MessageHeader>>(listOf())
    val isLoading = mutableStateOf(false)

    private val dummyList = listOf(
        MessageHeader("ABCDEF", listOf("ABC", "DEF"), "Fitness Club", "This dumbells are cool!"),
        MessageHeader("ABCDEF", listOf("ABC", "DEF"), "Pets", "Doberman are not cute"),
        MessageHeader("ABCDEF", listOf("ABC", "DEF"), "Cars", "I bought a Porshe"),
    )

    fun loadMessages() {
        isLoading.value = true
        viewModelScope.launch {
            delay(2000L)
            messageHeaders.value = dummyList
            isLoading.value = false
        }
    }

    fun updateMessages() {

    }
}