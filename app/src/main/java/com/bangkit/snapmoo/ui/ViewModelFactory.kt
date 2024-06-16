package com.bangkit.snapmoo.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.snapmoo.data.repo.MainRepository
import com.bangkit.snapmoo.di.Injection
import com.bangkit.snapmoo.ui.auth.login.LoginViewModel
import com.bangkit.snapmoo.ui.auth.register.RegisterViewModel
import com.bangkit.snapmoo.ui.home.HomeViewModel
import com.bangkit.snapmoo.ui.news.NewsViewModel
import com.bangkit.snapmoo.ui.profile.ProfileViewModel
import com.bangkit.snapmoo.ui.profile.bookmark.BookmarkViewModel
import com.bangkit.snapmoo.ui.profile.edit_password.EditPasswordViewModel
import com.bangkit.snapmoo.ui.profile.edit_profile.EditProfileViewModel
import com.bangkit.snapmoo.ui.profile.history.HistoryViewModel
import com.bangkit.snapmoo.ui.profile.setting.SettingViewModel
import com.bangkit.snapmoo.ui.report.detail.DetailReportViewModel
import com.bangkit.snapmoo.ui.report.ReportViewModel
import com.bangkit.snapmoo.ui.scan.scan_result.ScanResultViewModel
import com.bangkit.snapmoo.ui.scan.send_report.SendReportViewModel
import com.bangkit.snapmoo.ui.splash_screen.SplashScreenViewModel

class MainViewModelFactory(private val repository: MainRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> {
                SplashScreenViewModel(repository) as T
            }

            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }

            modelClass.isAssignableFrom(EditPasswordViewModel::class.java) -> {
                EditPasswordViewModel(repository) as T
            }

            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ScanResultViewModel::class.java) -> {
                ScanResultViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ReportViewModel::class.java) -> {
                ReportViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SendReportViewModel::class.java) -> {
                SendReportViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailReportViewModel::class.java) -> {
                DetailReportViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MainViewModelFactory? = null

        @JvmStatic

        fun getInstance(context: Context): MainViewModelFactory {
            if (INSTANCE == null) {
                synchronized(MainViewModelFactory::class.java) {
                    INSTANCE = MainViewModelFactory(Injection.provideMainRepository(context))
                }
            }
            return INSTANCE as MainViewModelFactory
        }
    }
}