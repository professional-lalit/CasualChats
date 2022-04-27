package com.casualchats.app.models


data class MessageDetail(
    val from: String,
    val to: String,
    val message: String,
    val resourceId: String? = null,
    val sentAt: Long
) {
    constructor() : this("", "", "", "", 0)
}