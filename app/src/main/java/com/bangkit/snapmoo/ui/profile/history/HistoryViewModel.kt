package com.bangkit.snapmoo.ui.profile.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository

class HistoryViewModel(private val repository: MainRepository) : ViewModel() {

    fun getAllHistory(token: String) = repository.getAllHistory(token)

    fun addBookmark(token: String, id: String, isSaved: Boolean) = repository.bookmarkHistory(token, id, isSaved)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}