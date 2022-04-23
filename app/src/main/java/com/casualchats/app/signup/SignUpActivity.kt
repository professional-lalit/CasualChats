package com.casualchats.app.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.casualchats.app.common.Screen
import com.casualchats.app.common.Utils.fileFromContentUri
import com.casualchats.app.common.Utils.showToast
import com.casualchats.app.otp.OTPActivity
import com.casualchats.app.ui.theme.CasualChatsTheme
import com.github.dhaval2404.imagepicker.ImagePicker

class SignUpActivity : ComponentActivity() {

    private val signUpVM: SignUpVM by viewModels()
    private var otp: String? = null

    private val otpActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //  you will get result here in result.data
                otp = result.data?.getStringExtra("otp")
                signUpVM.signInWithPhoneAuthCredential(this, otp!!)
            }
        }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    signUpVM.userImageUri.value = fileFromContentUri(this, fileUri).absolutePath
                }
                ImagePicker.RESULT_ERROR -> {
                    showToast(ImagePicker.getError(data))
                }
                else -> {
                    showToast("Task Cancelled")
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CasualChatsTheme {
                SignUpUI(
                    signUpVM.firstName,
                    signUpVM.lastName,
                    signUpVM.phoneNumber,
                    signUpVM.userImageUri,
                    signUpVM.isLoadingState,
                    onLoginNoteClicked = { finish() },
                    onSignUpClicked = { signUpVM.verifyPhoneNumber(this) },
                    onPhotoClicked = { getPhoto() }
                )
            }
        }

        signUpVM.navigateTo.observe(this) {
            if (it.clazz == OTPActivity::class.java) {
                otpActivityResult.launch(Intent(this, OTPActivity::class.java))
            } else {
                it.open(this)
            }
        }

        signUpVM.messageToShow.observe(this) {
            showToast(it)
        }
    }

    private fun getPhoto() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(
                1080,
                1080
            )
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

}
