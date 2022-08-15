package com.themans.street_stall.model

data class LoginResponse(
    val id: String,
    val nickname: String,
    val password : String,
    val user_state : String,
    val Guest : Boolean,
    val Stall : Boolean,
    val Manager : Boolean
)
