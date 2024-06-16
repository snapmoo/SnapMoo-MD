package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("data")
    val data: UserResult,

    @field:SerializedName("message")
    val message: String
)

data class UserResult(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("phone_number")
    val phoneNumber: String,

    @field:SerializedName("photo")
    val photo: String
)
