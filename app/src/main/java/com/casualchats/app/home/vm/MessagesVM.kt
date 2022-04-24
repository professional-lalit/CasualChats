package com.casualchats.app.home.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casualchats.app.common.Prefs
import com.casualchats.app.models.MessageHeader
import com.casualchats.app.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessagesVM : ViewModel() {

    val messageHeaders = mutableStateOf<List<MessageHeader>>(listOf())
    val isLoading = mutableStateOf(false)

    fun loadMessages() {
        isLoading.value = true

        val user = Firebase.auth.currentUser!!
        val db = Firebase.firestore

        db.collection("chat-headers")
            .document(user.uid)
            .collection("user-chat-headers")
            .get()
            .addOnSuccessListener {
                isLoading.value = false
                val headers = it.documents.map { it.toObject<MessageHeader>()!! }
                messageHeaders.value = headers
            }
            .addOnFailureListener {
                isLoading.value = false
            }
    }

    fun updateMessages() {

    }
}