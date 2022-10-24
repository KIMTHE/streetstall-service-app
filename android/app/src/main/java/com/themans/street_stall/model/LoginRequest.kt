package com.themans.street_stall.model

import com.squareup.moshi.Json

data class LoginRequest(
    @field:Json(name="userid") val id: String,
    @field:Json(name="nickname") val nickname: String
)
