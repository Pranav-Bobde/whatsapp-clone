package com.example.whatsapp_clone.models

data class Users(
    var profilepic: String? = null,
    var userName: String? = null,
    var mail: String? = null,
    var password: String? = null,
    var userId: String? = null,
    var lastMessage: String? = null,
    var status: String? = null
) {
    constructor(userName: String?, email: String?, password: String?) :
            this(
                null,
                userName,
                email,
                password,
                null,
                null,
                null
            )
}