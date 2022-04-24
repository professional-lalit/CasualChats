package com.casualchats.app.models

data class MessageHeader(
    val headerId: String,
    val participants: List<String>,
    val groupName: String? = null,
    val latestMessage: String,
    val msgCount: Int? = 0
)
