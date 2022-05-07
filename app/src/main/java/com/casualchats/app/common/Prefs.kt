package com.casualchats.app.common

import android.content.SharedPreferences
import com.casualchats.app.models.User

class Prefs(private val sharedPreferences: SharedPreferences) {

    private enum class PrefConst(val key: String) {
        USER_MODEL("user_model")
    }

    var user: User? = null
        get() {
            return User.toObjectUsingGson(
                sharedPreferences.getString(
                    PrefConst.USER_MODEL.key,
                    ""
                )!!
            )
        }
        set(value) {
            field = value
            sharedPreferences.edit().putString(
                PrefConst.USER_MODEL.key,
                User.toStringUsingGson(field!!)
            ).apply()
        }

}