package com.casualchats.app.common

import com.casualchats.app.models.User

object Prefs {

    private val sharedPreferences by lazy { CustomApplication.sharedPreferences!! }

    private enum class PrefConst(val key: String) {
        USER_MODEL("user_model")
    }

    var user: User? = null
        get() {
            return User("", "", "", "", "")
        }
        set(value) {
            field = value
        }

}