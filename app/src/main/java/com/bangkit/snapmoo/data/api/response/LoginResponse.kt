package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val data: LoginResult,

    @field:SerializedName("message")
    val message: String
)

data class LoginResult(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("photo")
    val photo: String,

    @field:SerializedName("token")
    val token: String
)
