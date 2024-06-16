package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

    @field:SerializedName("data")
    val data: List<HistoryResult>,

    @field:SerializedName("message")
    val message: String
)

data class HistoryResult(

    @field:SerializedName("id")
    val historyId: String,

    @field:SerializedName("result")
    val result: String,

    @field:SerializedName("score")
    val score: String,

    @field:SerializedName("created_at")
    val createdAt: CreatedAtHistoryResult,

    @field:SerializedName("photo")
    val photo: String,

    @field:SerializedName("is_saved")
    val isSaved: Boolean

)

data class CreatedAtHistoryResult(

    @field:SerializedName("_nanoseconds")
    val nanoseconds: Long? = null,

    @field:SerializedName("_seconds")
    val seconds: Long? = null
)