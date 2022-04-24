package com.casualchats.app.home.vm

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casualchats.app.common.Prefs
import com.casualchats.app.common.Screen
import com.casualchats.app.home.models.ProfileData
import com.casualchats.app.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class ProfileVM : ViewModel() {

    val TAG = this::class.java.simpleName

    val profileData = ProfileData(
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(false)
    )

    val messageToShow = MutableLiveData("")
    val isLoggedOut = MutableLiveData(false)

    init {
        Firebase.auth.addAuthStateListener {
            if (it.currentUser == null) {
                isLoggedOut.value = true
            }
        }
    }

    fun fetchUser() {
        profileData.isLoading.value = true
        val user = Firebase.auth.currentUser!!
        val db = Firebase.firestore

        db.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener {
                profileData.isLoading.value = false
                val userFetched = it.toObject<User>()!!
                Prefs.user = userFetched
                profileData.firstName.value = userFetched.firstName ?: ""
                profileData.lastName.value = userFetched.lastName ?: ""
                profileData.phoneNumber.value = userFetched.phoneNumber ?: ""
                profileData.userImageUri.value = userFetched.imageUrl ?: ""
            }
            .addOnFailureListener {
                profileData.isLoading.value = false
                messageToShow.value = "Failed to load user details"
            }
    }

    fun updateUser() {
        profileData.isLoading.value = true
        if (profileData.isImageUpdated.value) {
            uploadUserImage()
        } else {
            updateUserData(Prefs.user?.imageUrl ?: "")
        }
    }

    private fun updateUserData(imageUrl: String) {
        val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = profileData.firstName.value + " " + profileData.lastName.value
            photoUri = Uri.parse(imageUrl)
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->

                profileData.isLoading.value = false

                if (task.isSuccessful) {

                    //Update user in DB
                    val db = Firebase.firestore
                    val userToAdd = mapOf(
                        "userId" to user.uid,
                        "firstName" to profileData.firstName.value,
                        "lastName" to profileData.lastName.value,
                        "phoneNumber" to profileData.phoneNumber.value,
                        "imageUrl" to imageUrl,
                        "bio" to "I am an updated user."
                    )
                    db.collection("users")
                        .document(user.uid)
                        .update(userToAdd)
                        .addOnSuccessListener { documentReference ->
                            profileData.isLoading.value = false
                            profileData.isDataUpdated.value = false
                            messageToShow.value = "User updated successfully"
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                            profileData.isLoading.value = false
                            profileData.isDataUpdated.value = false
                            messageToShow.value = "User not updated"
                        }

                }
            }
    }

    private fun uploadUserImage() {
        val storageRef = Firebase.storage.reference
        val auth = Firebase.auth
        val profileImagesRef = storageRef.child("profile-images/${auth.currentUser?.uid}")

        val file = Uri.fromFile(File(profileData.userImageUri.value))
        val uploadTask = profileImagesRef.putFile(file)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profileImagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val user = Prefs.user
                user?.imageUrl = downloadUri.toString()
                Prefs.user = user
                messageToShow.value = "Profile image updated"
                updateUserData(user?.imageUrl ?: Prefs.user?.imageUrl ?: "")
            } else {
                messageToShow.value = "Profile image update failed"
            }
        }
    }

    fun logout() {
        Firebase.auth.signOut()
    }

}