package com.jesus.cproject.models

import android.security.identity.AccessControlProfile
import android.widget.ImageView
import java.util.*

data class Message(
    val authorId: String,
    val message: String,
    val profileImageURL: String,
    val sentAt: Date
) {
}