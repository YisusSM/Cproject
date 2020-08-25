package com.jesus.cproject.models

import java.util.*

data class Rate(
    val text: String = "",
    val rate: Float = 0f,
    val createAt: Date = Date(),
    val profileImgUrl: String = "",
    val userId: String = ""
)