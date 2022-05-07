package com.casualchats.app.search_users

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.casualchats.app.common.Prefs
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

    val headerId = MutableLiveData<String>()

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
            latestMessage = "",
            unReadMsgCount = 0,
            isRead = true
        )

        var isHeaderAssigned = false

        val user = Firebase.auth.currentUser!!
        val db = Firebase.firestore

        db.collection("chat-headers")
            .document(user.uid)
            .collection("user-chat-headers")
            .get()
            .addOnSuccessListener {
                val headers = it.documents.map { it.toObject<MessageHeader>()!! }

                for (header in headers) {
                    val list = header.participants?.filter { it.userId == otherUser.userId }
                    if (list?.isNotEmpty() == true) {
                        headerId.value = header.headerId!!
                        isHeaderAssigned = true
                        break
                    }
                }

                if (!isHeaderAssigned) {
                    db.collection("chat-headers")
                        .document(user.uid)
                        .collection("user-chat-headers")
                        .add(chatHeader)
                        .addOnSuccessListener {
                            headerId.value = it.id
                        }
                        .addOnFailureListener {

                        }
                }
            }
            .addOnFailureListener {

            }

    }

}