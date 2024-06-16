package com.bangkit.snapmoo.ui.profile.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: MainRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}