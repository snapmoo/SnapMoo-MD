package com.bangkit.snapmoo.ui.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository
import java.io.File

class EditProfileViewModel(private val repository: MainRepository) : ViewModel() {

    fun getUserData(token: String) = repository.getUserData(token)

    fun editUserData(token: String, name: String, phoneNumber: String, image: File?) = repository.editUserData(token, name, phoneNumber, image)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}