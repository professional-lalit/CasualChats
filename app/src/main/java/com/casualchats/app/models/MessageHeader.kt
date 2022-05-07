package com.casualchats.app.models

data class MessageHeader(
    var headerId: String? = null,
    val participants: List<User>? = null,
    val groupName: String? = null,
    var latestMessage: LatestMessage? = null,
    val unReadMsgCount: Int? = 0,
    var isRead: Boolean? = false
) {
    constructor() : this("", listOf(), "", latestMessage = LatestMessage(), 0)
}
