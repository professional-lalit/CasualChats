package com.casualchats.app.models

data class MessageHeader(
    var headerId: String? = null,
    val participants: List<User>? = null,
    val groupName: String? = null,
    val latestMessage: String? = null,
    val unReadMsgCount: Int? = 0,
    val isRead: Boolean? = false
) {
    constructor() : this("", listOf(), "", "", 0)
}
