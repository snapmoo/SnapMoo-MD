package com.bangkit.snapmoo.ui.profile.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository

class BookmarkViewModel(private val repository: MainRepository) : ViewModel() {
    fun getSavedHistory(token: String) = repository.getSavedHistory(token)

    fun deleteBookmark(token: String, id: String, isSaved: Boolean) = repository.bookmarkHistory(token, id, isSaved)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}