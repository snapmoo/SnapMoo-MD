package com.bangkit.snapmoo.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.data.api.response.DetailReportResponse
import com.bangkit.snapmoo.data.api.response.HistoryResponse
import com.bangkit.snapmoo.data.api.response.LoginResponse
import com.bangkit.snapmoo.data.api.response.NewsResponse
import com.bangkit.snapmoo.data.api.response.PredictionResponse
import com.bangkit.snapmoo.data.api.response.ReportResponse
import com.bangkit.snapmoo.data.api.response.UserResponse
import com.bangkit.snapmoo.data.api.retrofit.MainApiService
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class MainRepository(
    private val userPreference: UserPreference,
    private val mainApiService: MainApiService,
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(
        email: String,
        password: String,
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.login(email, password)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        phoneNumber: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.register(name, email, password, phoneNumber)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getUserData(token: String): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.getUserData("Bearer $token")
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, NewsResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun editUserData(token: String, name: String, phoneNumber: String, image: File?): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)


        val imagePart = if (image != null) {
            val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photo", image.name, requestFile)
        } else {
            MultipartBody.Part.createFormData("photo", "")
        }

//        val requestImageFile = image?.asRequestBody("image/jpeg".toMediaTypeOrNull())
//        val imageBody =
//            MultipartBody.Part.createFormData("photo", image?.name, requestImageFile)

        try {
            val nameBody = name.toRequestBody("text/plain".toMediaType())
            val phoneNumberBody = phoneNumber.toRequestBody("text/plain".toMediaType())
            val result = mainApiService.editUserData("Bearer $token", nameBody, phoneNumberBody, imagePart)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getAllNews(token: String): LiveData<Result<NewsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.getAllNews("Bearer $token")
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, NewsResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun searchNews(token: String, query: String): LiveData<Result<NewsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.searchNews("Bearer $token", query)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, NewsResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun postHistory(token: String, indication: String, percentage: Int, image: File): LiveData<Result<PredictionResponse>> = liveData {
        emit(Result.Loading)

        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val imageBody =
            MultipartBody.Part.createFormData("photo", image.name, requestImageFile)

        try {
            val indicationBody = indication.toRequestBody("text/plain".toMediaType())
            val percentageBody = percentage.toString().toRequestBody("text/plain".toMediaType())
            val result = mainApiService.postHistory("Bearer $token", indicationBody, percentageBody, imageBody)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, PredictionResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getAllHistory(token: String): LiveData<Result<HistoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.getAllHistory("Bearer $token")
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, HistoryResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getSavedHistory(token: String): LiveData<Result<HistoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.getSavedHistory("Bearer $token")
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, HistoryResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun bookmarkHistory(token: String, id: String, isSaved: Boolean): LiveData<Result<HistoryResponse>> = liveData {
        emit(Result.Loading)
        Log.d("bookmarkHistory", "isSaved: $isSaved")
        try {
            val result = mainApiService.bookmarkHistory("Bearer $token", id, isSaved)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, HistoryResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getAllReport(token: String): LiveData<Result<ReportResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.getAllReport("Bearer $token")
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ReportResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getDetailReport(token: String, id: String): LiveData<Result<DetailReportResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = mainApiService.getDetailReport("Bearer $token", id)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ReportResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun uploadReportData(
        token: String,
        name: String,
        phoneNumber: String,
        email: String,
        address: String,
        city: String,
        province: String,
        latitude: Double?,
        longitude: Double?,
        manyHave: Int,
        condition: String,
        prediction: String,
        score: Int,
        image: File,
    ): LiveData<Result<ReportResponse>> = liveData {
        emit(Result.Loading)
        try {
            val nameBody = name.toRequestBody("text/plain".toMediaType())
            val phoneNumberBody = phoneNumber.toRequestBody("text/plain".toMediaType())
            val emailBody = email.toRequestBody("text/plain".toMediaType())
            val addressBody = address.toRequestBody("text/plain".toMediaType())
            val cityBody = city.toRequestBody("text/plain".toMediaType())
            val provinceBody = province.toRequestBody("text/plain".toMediaType())
            val latitudeBody = latitude?.toString()?.toRequestBody("text/plain".toMediaType())
            val longitudeBody = longitude?.toString()?.toRequestBody("text/plain".toMediaType())
            val manyHaveBody = manyHave.toString().toRequestBody("text/plain".toMediaType())
            val conditionBody = condition.toRequestBody("text/plain".toMediaType())
            val predictionBody = prediction.toRequestBody("text/plain".toMediaType())
            val scoreBody = score.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("photo", image.name, requestImageFile)

            val result = mainApiService.uploadReportData(
                "Bearer $token",
                nameBody,
                phoneNumberBody,
                emailBody,
                addressBody,
                cityBody,
                provinceBody,
                latitudeBody,
                longitudeBody,
                manyHaveBody,
                conditionBody,
                predictionBody,
                scoreBody,
                multipartBody
            )
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ReportResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            userPreference: UserPreference, mainApiService: MainApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(userPreference, mainApiService)
            }.also { instance = it }
    }
}