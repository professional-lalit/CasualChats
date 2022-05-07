package com.casualchats.app.models

import com.google.gson.Gson


data class User(
    var firstName: String? = null,
    var lastName: String? = null,
    var imageUrl: String? = null,
    var phoneNumber: String? = null,
    var userId: String? = null
) {
    constructor() : this("", "", "", "", "")

    companion object {
        fun toStringUsingGson(user: User): String {
            return Gson().toJson(user)
        }

        fun toObjectUsingGson(string: String): User {
            return Gson().fromJson(string, User::class.java)
        }
    }
}
