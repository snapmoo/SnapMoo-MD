package com.bangkit.snapmoo.data.pref

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val photo: String,
    val token: String,
    val isLogin: Boolean = false,
)