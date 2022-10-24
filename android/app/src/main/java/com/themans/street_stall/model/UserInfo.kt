package com.themans.street_stall.model

import com.squareup.moshi.Json

data class UserInfo(
    @field:Json(name="userid") val id: String,
    @field:Json(name="nickname") val nickname: String,
    @field:Json(name="userstate") val userState : Int,
    @field:Json(name="guest") val isPosGuest : Boolean,
    @field:Json(name="stall") val isPosStall : Boolean,
    @field:Json(name="manager") val isPosManager : Boolean
)
