package com.casualchats.app.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.casualchats.app.common.Screen
import com.casualchats.app.common.Utils.showToast
import com.casualchats.app.home.HomeActivity
import com.casualchats.app.otp.OTPActivity
import com.casualchats.app.signup.SignUpActivity
import com.casualchats.app.ui.theme.CasualChatsTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : ComponentActivity() {

    private val loginVM: LoginVM by viewModels()

    private val otpActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //  you will get result here in result.data
                val otp = result.data?.getStringExtra("otp")
                loginVM.signInWithPhoneAuthCredential(this, otp!!)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CasualChatsTheme {
                LoginUI(
                    loginVM.phoneNumber,
                    loginVM.isLoadingState,
                    onSignUpNoteClicked = { loginVM.openSignUp() },
                    onLoginClicked = { onLoginClicked() },
                    onPhoneNumberChanged = { loginVM.phoneNumber.value = it }
                )
            }
        }

        if (Firebase.auth.currentUser != null) {
            Screen.Home().open(this)
            finish()
        }

        loginVM.messageToShow.observe(this) { showToast(it) }

        loginVM.navigateTo.observe(this) {
            when (it.clazz) {
                OTPActivity::class.java -> {
                    otpActivityResult.launch(Intent(this, OTPActivity::class.java))
                }
                HomeActivity::class.java -> {
                    it.open(this)
                    finish()
                }
                else -> {
                    it.open(this)
                }
            }

        }

    }

    private fun onLoginClicked() {
        loginVM.verifyPhoneNumber(this)
    }
}




