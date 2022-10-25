package com.themans.street_stall.model

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name="userid") val id: String,
    @Json(name="nickname") val nickname: String
)
