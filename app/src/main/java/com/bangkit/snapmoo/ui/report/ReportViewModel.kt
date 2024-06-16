package com.bangkit.snapmoo.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository

class ReportViewModel(private val repository: MainRepository) : ViewModel() {

    fun getAllReport(token: String) = repository.getAllReport(token)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}