package com.themans.street_stall.model

import com.squareup.moshi.Json

data class UserInfo(
    @Json(name="userid") val id: String,
    @Json(name="nickname") val nickname: String,
    @Json(name="userstate") val userState : Int,
    @Json(name="guest") val isPosGuest : Boolean,
    @Json(name="stall") val isPosStall : Boolean,
    @Json(name="manager") val isPosManager : Boolean
)
