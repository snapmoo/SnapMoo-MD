package com.bangkit.snapmoo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository

class HomeViewModel(private val repository: MainRepository) : ViewModel() {

    fun getUserData(token: String) = repository.getUserData(token)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}