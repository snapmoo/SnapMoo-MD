package com.bangkit.snapmoo.ui.scan.send_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository
import java.io.File

class SendReportViewModel(private val repository: MainRepository) : ViewModel() {
    fun getUserData(token: String) = repository.getUserData(token)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
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
    ) = repository.uploadReportData(
        token,
        name,
        phoneNumber,
        email,
        address,
        city,
        province,
        latitude,
        longitude,
        manyHave,
        condition,
        prediction,
        score,
        image
    )
}