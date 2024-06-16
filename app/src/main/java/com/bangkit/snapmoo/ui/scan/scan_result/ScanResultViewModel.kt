package com.bangkit.snapmoo.ui.scan.scan_result

import android.health.connect.datatypes.units.Percentage
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository
import java.io.File

class ScanResultViewModel(private val repository: MainRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun postHistory(token: String, indication: String, percentage: Int, image: File) = repository.postHistory(token, indication, percentage, image)

    private val _hasUploaded = MutableLiveData<Boolean>(false)
    val hasUploaded: LiveData<Boolean> get() = _hasUploaded

    fun setUploadedStatus(status: Boolean) {
        _hasUploaded.value = status
    }

}