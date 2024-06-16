package com.bangkit.snapmoo.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.databinding.ActivityWelcomeBinding
import com.bangkit.snapmoo.ui.auth.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGettingStarted.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}