package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName

data class SaveHistoryResponse(

    @field:SerializedName("data")
    val data: SaveHistoryResult,

    @field:SerializedName("message")
    val message: String
)

data class SaveHistoryResult(

    @field:SerializedName("is_saved")
    val isSaved: Boolean,
)