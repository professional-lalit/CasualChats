package com.casualchats.app.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.casualchats.app.otp.OTPActivity
import com.casualchats.app.signup.SignUpActivity
import com.casualchats.app.ui.theme.CasualChatsTheme

class LoginActivity : ComponentActivity() {

    private val loginVM: LoginVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CasualChatsTheme {
                LoginUI(
                    loginVM.mobileNumber,
                    loginVM.isLoadingState,
                    onSignUpNoteClicked = { loginVM.openSignUp() },
                    onLoginClicked = { onLoginClicked() },
                    onPhoneNumberChanged = { loginVM.mobileNumber.value = it }
                )
            }
        }

        loginVM.navigateTo.observe(this) { it.open(this) }

        loginVM.mobileNumber

    }

    private fun onLoginClicked() {
        loginVM.login()
    }
}




