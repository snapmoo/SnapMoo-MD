package com.bangkit.snapmoo.ui.report.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository

class DetailReportViewModel(private val repository: MainRepository) : ViewModel() {

    fun getDetailReport(token: String, id: String) = repository.getDetailReport(token, id)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}