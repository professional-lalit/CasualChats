package com.casualchats.app.home.vm

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.casualchats.app.common.Prefs
import com.casualchats.app.common.Utils
import com.casualchats.app.models.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessagesVM @Inject constructor(
    private val prefs: Prefs
) : ViewModel() {
    val TAG = MessagesVM::class.java.simpleName

    val resource = mutableStateOf<ResourceMeta?>(null)

    val messageHeaders = mutableStateOf<List<MessageHeader>>(listOf())
    val messages = mutableStateOf<List<MessageDetail>>(listOf())

    val isLoading = mutableStateOf(false)
    val isFileUploading = mutableStateOf(false)

    fun loadMessageHeaders() {
        isLoading.value = true

        val db = Firebase.firestore

        db.collection("chat-headers-${prefs.user?.userId!!}")
            .get()
            .addOnSuccessListener {
                isLoading.value = false
                val headers = it.documents.map { it.toObject<MessageHeader>()!! }
                it.documents.forEachIndexed { index, documentSnapshot ->
                    headers[index].headerId = documentSnapshot.id
                }
                messageHeaders.value = headers
            }
            .addOnFailureListener {
                isLoading.value = false
            }
    }

    fun sendMessage(
        msg: String,
        otherUserId: String,
        headerId: String
    ) {

        if (resource.value != null) {
            uploadFile(headerId) {
                sendText(headerId, otherUserId, msg, it)
            }
        } else {
            sendText(headerId, otherUserId, msg)
        }
    }

    private fun sendText(
        headerId: String,
        otherUserId: String,
        msg: String,
        resourceMeta: ResourceMeta? = null
    ) {
        val database = Firebase.database.reference
        val chatMessages = database.child(headerId)
        chatMessages.push().setValue(
            MessageDetail(
                from = prefs.user!!,
                to = otherUserId,
                message = msg,
                resource = resourceMeta,
                sentAt = Date().time
            )
        ).addOnSuccessListener {
            Log.d(TAG, "msg sent")
        }.addOnFailureListener {
            Log.d(TAG, "msg not sent, failed")
        }

        updateLatestMessageInHeader(headerId, otherUserId, msg)
    }

    private fun uploadFile(headerId: String, callback: (ResourceMeta) -> Unit) {

        isFileUploading.value = true

        val storageRef = Firebase.storage.reference
        val profileImagesRef = storageRef.child("attachments/${headerId}")

        val file = File(resource.value?.resourcePath!!)
        val uri = Uri.fromFile(file)
        val uploadTask = profileImagesRef.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profileImagesRef.downloadUrl
        }.addOnCompleteListener { task ->

            isFileUploading.value = false

            if (task.isSuccessful) {
                val downloadUri = task.result

                val resourceMeta = ResourceMeta(
                    title = file.name,
                    resourcePath = downloadUri.toString(),
                    resourceType = Utils.getResourceType(file),
                    resourceSize = file.length()
                )

                callback.invoke(resourceMeta)
                resource.value = null
            }
        }
    }

    private fun updateLatestMessageInHeader(headerId: String, otherUserId: String, msg: String) {
        val user = Firebase.auth.currentUser!!
        val db = Firebase.firestore

        val latestMsg = LatestMessage(user.uid, otherUserId, msg)

        db.collection("chat-headers-${prefs.user?.userId!!}")
            .document(headerId)
            .get()
            .addOnSuccessListener {
                val header = it.toObject<MessageHeader>()

                if (header != null) {

                    latestMsg.from = prefs.user?.userId ?: ""

                    header.headerId = headerId
                    header.latestMessage = latestMsg
                    header.isRead = false

                    db.collection("chat-headers-${prefs.user?.userId!!}")
                        .document(header.headerId!!)
                        .set(header)
                        .addOnSuccessListener {
                            Log.d(TAG, "latest msg updated")
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "latest msg not updated")
                        }
                }

            }
    }

    fun loadMessages(headerId: String) {
        val database = Firebase.database.reference
        val chatMessages = database.child(headerId)
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
            Log.e(TAG, ex.localizedMessage ?: "")
            isLoading.value = false
        }
    }


}