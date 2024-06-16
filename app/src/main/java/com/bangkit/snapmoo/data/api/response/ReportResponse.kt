package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue

data class ReportResponse(

    @field:SerializedName("data")
    val data: List<ReportResult>,

    @field:SerializedName("message")
    val message: String
)

data class ReportResult(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("many_have")
    val manyHave: String? = null,

    @field:SerializedName("score")
    val score: @RawValue Any? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("province")
    val province: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("affected_body_part")
    val affectedBodyPart: String? = null,

    @field:SerializedName("latitude")
    val latitude: Any? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("photo")
    val photo: Any? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("longitude")
    val longitude: Any? = null,

    @field:SerializedName("prediction")
    val prediction: Any? = null,

    @field:SerializedName("createdAt")
    val createdAt: CreatedAtReportResult? = null
)

data class CreatedAtReportResult(

    @field:SerializedName("_nanoseconds")
    val nanoseconds: Long? = null,

    @field:SerializedName("_seconds")
    val seconds: Long? = null
)
