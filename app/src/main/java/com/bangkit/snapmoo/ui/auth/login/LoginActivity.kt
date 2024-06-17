package com.bangkit.snapmoo.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.databinding.ActivityLoginBinding
import com.bangkit.snapmoo.ui.MainActivity
import com.bangkit.snapmoo.ui.auth.register.RegisterActivity
import com.bangkit.snapmoo.ui.MainViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {

        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailEditText.error = getString(R.string.email_cannot_be_empty)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = getString(R.string.invalid_email_format)
            return
        }
        if (password.isEmpty()) {
            binding.passwordEditText.error = getString(R.string.password_cannot_be_empty)
            return
        }
        if (password.length < 6) {
            binding.passwordEditText.error = "Password must contain at least 6 characters"
            return
        }

        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        val user = result.data.data
                        var url = ""
                        if (user.photo == null) {
                            url = ""
                        } else {
                            url = user.photo
                        }
                        loginViewModel.saveSession(
                            UserModel(
                                user.userId,
                                user.name,
                                user.email,
                                url,
                                user.token,
                                true
                            )
                        )
                        showLoading(false)
                        showToast(getString(R.string.login_success))
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
    }
}