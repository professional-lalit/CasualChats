package com.casualchats.app.models


data class MessageDetail(
    val fromId: String,
    val toId: String,
    val message: String,
    val resourceId: String? = null,
    val sentAt: Long
)