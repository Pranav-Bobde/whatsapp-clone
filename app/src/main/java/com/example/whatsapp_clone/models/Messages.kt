package com.example.whatsapp_clone.models

data class Messages(
    var uid: String? = null,
    var message: String? = null,
    var timeStamp: Long? = null,
    var messageId: String? = null
) {
    constructor(uid: String, message: String, timeStamp: Long) : this(
        uid,
        message,
        timeStamp,
        null
    )
}