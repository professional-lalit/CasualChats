package com.casualchats.app.common

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.casualchats.app.home.HomeActivity
import com.casualchats.app.login.LoginActivity
import com.casualchats.app.otp.OTPActivity
import com.casualchats.app.signup.SignUpActivity

sealed class Screen(val title: String, val clazz: Class<out ComponentActivity>) {

    fun open(from: ComponentActivity, bundle: Bundle? = null) {
        val intent = Intent(from, clazz)
        intent.putExtra("bundle", bundle)
        from.startActivity(intent)
    }

    class Login : Screen("Login", LoginActivity::class.java)
    class SignUp : Screen("SignUp", SignUpActivity::class.java)
    class Otp : Screen("Otp", OTPActivity::class.java)
    class Home : Screen("Home", HomeActivity::class.java)
}