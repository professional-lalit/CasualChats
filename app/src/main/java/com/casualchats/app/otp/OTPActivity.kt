package com.casualchats.app.otp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.casualchats.app.login.LoginActivity
import com.casualchats.app.ui.theme.CasualChatsTheme

class OTPActivity : ComponentActivity() {

    private val otpVM: OtpVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CasualChatsTheme {
                OtpUI(
                    otp = otpVM.otp,
                    isLoading = otpVM.isLoadingState,
                    onSubmitted = {
                        val intent = Intent().apply {
                            putExtra("otp", otpVM.otp.value)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    },
                    onBackClicked = { finish() })
            }
        }

        otpVM.navigateTo.observe(this) {
            if (it.clazz == LoginActivity::class.java) {
                finish()
            }
        }
    }
}


