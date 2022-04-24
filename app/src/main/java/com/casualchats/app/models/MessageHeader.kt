package com.casualchats.app.models

data class MessageHeader(
    val headerId: String,
    val participants: List<String>,
    val groupName: String? = null,
    val latestMessage: String,
    val unReadMsgCount: Int? = 0,
    val isRead: Boolean? = false
) {
    constructor() : this("", listOf(), "", "", 0)
}
