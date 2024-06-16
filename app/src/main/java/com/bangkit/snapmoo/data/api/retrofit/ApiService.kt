package com.bangkit.snapmoo.data.api.retrofit

import com.bangkit.snapmoo.data.api.response.DetailReportResponse
import com.bangkit.snapmoo.data.api.response.HistoryResponse
import com.bangkit.snapmoo.data.api.response.LoginResponse
import com.bangkit.snapmoo.data.api.response.NewsResponse
import com.bangkit.snapmoo.data.api.response.PredictionResponse
import com.bangkit.snapmoo.data.api.response.ReportResponse
import com.bangkit.snapmoo.data.api.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone_number") phoneNumber: String
    ): LoginResponse

    @GET("user")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ): UserResponse

    @Multipart
    @PUT("user")
    suspend fun editUserData(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part file: MultipartBody.Part
    ): UserResponse

    @GET("articles")
    suspend fun getAllNews(
        @Header("Authorization") token: String
    ): NewsResponse

    @GET("articles/search")
    suspend fun searchNews(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): NewsResponse

    @Multipart
    @POST("history")
    suspend fun postHistory(
        @Header("Authorization") token: String,
        @Part("result") result: RequestBody,
        @Part("score") score: RequestBody,
        @Part file: MultipartBody.Part
    ): PredictionResponse

    @GET("history")
    suspend fun getAllHistory(
        @Header("Authorization") token: String
    ): HistoryResponse

    @GET("history/saved")
    suspend fun getSavedHistory(
        @Header("Authorization") token: String
    ): HistoryResponse

    @FormUrlEncoded
    @PUT("history/save/{id}")
    suspend fun bookmarkHistory(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("is_saved") isSaved: Boolean
    ): HistoryResponse

    @GET("report")
    suspend fun getAllReport(
        @Header("Authorization") token: String
    ): ReportResponse

    @GET("report/{id}")
    suspend fun getDetailReport(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailReportResponse

    @Multipart
    @POST("report")
    suspend fun uploadReportData(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part("province") province: RequestBody,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("many_have") manyHave: RequestBody,
        @Part("affected_body_part") condition: RequestBody,
        @Part("prediction") prediction: RequestBody,
        @Part("score") score: RequestBody,
        @Part file: MultipartBody.Part,
    ): ReportResponse

}