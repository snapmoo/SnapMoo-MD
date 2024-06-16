package com.bangkit.snapmoo.di

import android.content.Context
import com.bangkit.snapmoo.data.pref.UserPreference
import com.bangkit.snapmoo.data.pref.dataStore
import com.bangkit.snapmoo.data.repo.MainRepository
import com.bangkit.snapmoo.data.api.retrofit.ApiConfig

object Injection {
    fun provideMainRepository(context: Context): MainRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val mainApiService = ApiConfig.getMainApiService()
        return MainRepository.getInstance(pref, mainApiService)
    }
}