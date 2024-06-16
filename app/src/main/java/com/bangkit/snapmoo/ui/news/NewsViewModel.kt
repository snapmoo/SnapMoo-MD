package com.bangkit.snapmoo.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository

class NewsViewModel(private val repository: MainRepository) : ViewModel() {
    fun getAllNews(token: String) = repository.getAllNews(token)

    fun searchNews(token: String, query: String) = repository.searchNews(token, query)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}