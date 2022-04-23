package com.casualchats.app.signup

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casualchats.app.common.Screen
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 1. Sign in user
 * 2. Upload user image and get the url
 * 3. Register user in db
 */
class SignUpVM : ViewModel() {

    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var storedVerificationId: String? = null
    private val TAG = this::class.java.simpleName

    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val phoneNumber = mutableStateOf("")
    val userImageUri = mutableStateOf("")
    val isLoadingState = mutableStateOf(false)

    val navigateTo = MutableLiveData<Screen>()
    val messageToShow = MutableLiveData<String>()
    val beginSignIn = MutableLiveData(false)

    private val auth = Firebase.auth

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            messageToShow.value = "Phone number verified"
            beginSignIn.value = true
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }
            // Show a message and update the UI
            messageToShow.value = "Authentication failed"
            isLoadingState.value = false
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
            messageToShow.value = "An OTP has been sent your phone"
            navigateTo.value = Screen.Otp()
        }
    }

    fun signInWithPhoneAuthCredential(activity: SignUpActivity, code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    messageToShow.value = "Sign in successful"
                    if (userImageUri.value.isNotEmpty()) {
                        uploadUserImage()
                    } else {
                        register("")
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                    messageToShow.value = "Sign in failed"
                }
            }
    }

    fun verifyPhoneNumber(activity: SignUpActivity) {
        isLoadingState.value = true
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91${phoneNumber.value}")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun uploadUserImage() {
        val storageRef = Firebase.storage.reference
        val profileImagesRef = storageRef.child("profile-images/${auth.currentUser?.uid}")

        val file = Uri.fromFile(File(userImageUri.value))
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
                register(downloadUri.toString())
            } else {
                register("")
            }
        }
    }

    private fun register(imageUrl: String) {
        //Update the auth record
        val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = firstName.value + " " + lastName.value
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }

        //Create user in DB
        val db = Firebase.firestore
        val userToAdd = hashMapOf(
            "userId" to user.uid,
            "firstName" to firstName.value,
            "lastName" to lastName.value,
            "phoneNumber" to phoneNumber.value,
            "imageUrl" to imageUrl,
            "bio" to "I am a new user."
        )
        db.collection("users")
            .add(userToAdd)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                messageToShow.value = "User registered successfully"
                isLoadingState.value = false
                navigateTo.value = Screen.Home()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                isLoadingState.value = false
                messageToShow.value = "User not registered"
            }
    }

}