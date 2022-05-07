package com.casualchats.app.search_users

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.casualchats.app.common.Prefs
import com.casualchats.app.models.LatestMessage
import com.casualchats.app.models.MessageHeader
import com.casualchats.app.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchUsersVM @Inject constructor(private val prefs: Prefs) : ViewModel() {

    val users = mutableStateOf<List<User>>(listOf())
    val loading = mutableStateOf(false)

    val chatDetails = MutableLiveData<Pair<String, String>>()

    fun fetchUsers() {
        loading.value = true
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { list ->
                loading.value = false
                users.value = list.toObjects()
            }
            .addOnFailureListener {
                loading.value = false
            }
    }

    fun createMessageHeader(otherUser: User) {

        val chatHeader = MessageHeader(
            participants = listOf(prefs.user!!, otherUser),
            groupName = otherUser.firstName + " " + otherUser.lastName,
            latestMessage = LatestMessage(prefs.user?.userId!!, otherUser.userId!!, ""),
            unReadMsgCount = 0,
            isRead = true
        )

        var isHeaderAssigned = false

        val user = Firebase.auth.currentUser!!
        val db = Firebase.firestore

        db.collection("chat-headers")
            .get()
            .addOnSuccessListener {
                val headers = it.documents.map { it.toObject<MessageHeader>()!! }

                for (header in headers) {
                    val list = header.participants?.filter { it.userId == otherUser.userId }
                    if (list?.isNotEmpty() == true) {
                        chatDetails.value = Pair(header.headerId!!, otherUser.userId!!)
                        isHeaderAssigned = true
                        break
                    }
                }

                if (!isHeaderAssigned) {
                    db.collection("chat-headers")
                        .add(chatHeader)
                        .addOnSuccessListener {
                            chatDetails.value = Pair(it.id, otherUser.userId!!)
                        }
                        .addOnFailureListener {

                        }
                }
            }
            .addOnFailureListener {

            }

    }

}