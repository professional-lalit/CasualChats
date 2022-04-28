package com.casualchats.app.home.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.casualchats.app.models.Attachment
import com.casualchats.app.models.MessageDetail
import com.casualchats.app.models.MessageHeader
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class MessagesVM : ViewModel() {

    val attachment = mutableStateOf<Attachment?>(null)

    val messageHeaders = mutableStateOf<List<MessageHeader>>(listOf())
    val messages = mutableStateOf<List<MessageDetail>>(listOf())

    val isLoading = mutableStateOf(false)
    val isFileUploading = mutableStateOf(false)
    val TAG = MessagesVM::class.java.simpleName

    fun loadMessageHeaders() {
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

    fun sendMessage(msg: String, otherUserId: String) {

        val myId = Firebase.auth.currentUser?.uid!!

        val database = Firebase.database.reference
        val chatMessages = database.child("Axwdasc23dfsdfsdfsdfsd231")
        chatMessages.push().setValue(
            MessageDetail(
                from = myId,
                to = otherUserId,
                message = msg,
                resourceId = null,
                sentAt = Date().time
            )
        ).addOnSuccessListener {
            Log.d(TAG, "msg sent")
        }.addOnFailureListener {
            Log.d(TAG, "msg not sent, failed")
        }
    }

    fun loadMessages() {
        val database = Firebase.database.reference
        val chatMessages = database.child("Axwdasc23dfsdfsdfsdfsd231")
        chatMessages.addValueEventListener(chatMessagesListener)
        chatMessages.get().addOnSuccessListener {
            setLoadedMessages(it)
        }.addOnFailureListener {
            Log.d(TAG, "Failed : ${it.localizedMessage}")
        }
        isLoading.value = true
    }

    private val chatMessagesListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            setModifiedMessages(dataSnapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            isLoading.value = false
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    }

    private fun setModifiedMessages(dataSnapshot: DataSnapshot) {
        try {
            val msgs = mutableListOf<MessageDetail>()
            for (valRes in dataSnapshot.children) {
                val messageDetail = valRes.getValue<MessageDetail>()
                msgs.add(messageDetail!!)
            }
            messages.value = msgs
        } catch (ex: Exception) {
            Log.e(TAG, ex.localizedMessage)
            isLoading.value = false
        }
    }

    private fun setLoadedMessages(it: DataSnapshot) {
        try {
            val value = it.getValue<List<MessageDetail>>()
            messages.value = value!!
            isLoading.value = false
            Log.d(TAG, "Value is: $value")
        } catch (ex: Exception) {
            Log.e(TAG, ex.localizedMessage)
            isLoading.value = false
        }
    }
}