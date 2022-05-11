package com.casualchats.app.models

import com.casualchats.app.common.Utils


data class MessageDetail(
    val from: User,
    val to: String,
    val message: String,
    val resource: ResourceMeta? = null,
    val sentAt: Long
) {
    constructor() : this(User(), "", "", null, 0)
}

data class LatestMessage(
    var from: String,
    val to: String,
    val latestMessage: String,
    val resource: ResourceMeta? = null
) {
    constructor() : this("", "", "")
}

data class ResourceMeta(
    val title: String,
    val resourcePath: String,
    val resourceType: Utils.ResourceType,
    val resourceSize: Long
) {
    constructor() : this("", "", Utils.ResourceType.UNKNOWN, 0)
}