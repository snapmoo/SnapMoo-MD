package com.bangkit.snapmoo.data.pref

data class UserModel(
    val id: String,
    val token: String,
    val isLogin: Boolean = false,
)