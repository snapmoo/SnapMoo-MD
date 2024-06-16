package com.bangkit.snapmoo.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.databinding.ActivitySplashScreenBinding
import com.bangkit.snapmoo.ui.MainActivity
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.ui.welcome.WelcomeActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashScreenViewModel by viewModels<SplashScreenViewModel> {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            splashScreenViewModel.getSession().observe(this) { user ->
                if (!user.isLogin) {
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, SPLASH_DELAY)
    }

    companion object {
        const val SPLASH_DELAY: Long = 3000
    }

}