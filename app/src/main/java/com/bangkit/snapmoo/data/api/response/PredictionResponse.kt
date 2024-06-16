package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

    @field:SerializedName("data")
    val data: PredictionResult,

    @field:SerializedName("message")
    val message: String
)

data class PredictionResult(

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("prediction_id")
    val predictionId: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null

)
