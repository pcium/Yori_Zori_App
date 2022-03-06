package com.e.yorizori.Class

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var name: String?,
    var email: String?,
    var pw: String?
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "pw" to pw
        )
    }
}