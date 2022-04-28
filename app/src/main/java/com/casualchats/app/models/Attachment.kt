package com.casualchats.app.models

data class Attachment(
    val name: String,
    val path: String,
    val mimeType: String
) {

    enum class ResourceType(val type: String) {
        VIDEO("video"), AUDIO("audio"), DOCUMENT("document")
    }

}
