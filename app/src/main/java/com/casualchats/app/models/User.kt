package com.casualchats.app.models


data class User(
    var firstName: String?,
    var lastName: String?,
    var imageUrl: String?,
    var phoneNumber: String?,
    var userId: String?
) {
    constructor() : this("", "", "", "", "")
}
