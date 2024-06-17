package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

    @field:SerializedName("data")
    val data: PredictionResult,

    @field:SerializedName("message")
    val message: String
)

data class PredictionResult(


    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("is_saved")
    val isSaved: Boolean

)
