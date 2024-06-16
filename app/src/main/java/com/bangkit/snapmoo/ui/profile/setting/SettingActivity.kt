package com.bangkit.snapmoo.ui.profile.setting

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.databinding.ActivitySettingBinding
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.ui.welcome.WelcomeActivity

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingBinding
    private val settingViewModel by viewModels<SettingViewModel> {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setupButtonAction()
    }

    private fun setupButtonAction() {
        binding.viewLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.viewClearCache.setOnClickListener {
            clearAppCache()
        }
        binding.viewHelp.setOnClickListener {
            showEmailForHelp()
        }
        binding.viewLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showEmailForHelp() {
        val recipient = "snapmoo@gmail.com"
        val subject = "Request for Help and Support"
//        val message = ""

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
//            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(emailIntent)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.logout)
            setMessage(getString(R.string.logout_desc))
            setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                settingViewModel.logout()
                showToast(getString(R.string.logout_success))
                val intent = Intent(this@SettingActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            create()
            show()
        }
    }

    private fun clearAppCache() {
        try {
            AlertDialog.Builder(this).apply {
                setTitle("Clear cache")
                setMessage("Are you sure want to delete application cache?")
                setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                    cacheDir.deleteRecursively()
                    showToast("Clear cache success")
                }
                setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                create()
                show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun setToolbar() {
        binding.toolbar.btnBackToolbar.setOnClickListener {
            onBackPressed()
        }
        val label = getActivityLabel()
        binding.toolbar.tvToolbarTitle.text = label
    }

    private fun getActivityLabel(): String {
        return try {
            val activityInfo = packageManager.getActivityInfo(componentName, 0)
            activityInfo.loadLabel(packageManager).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            title.toString()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back_toolbar -> {
                finish()
            }
        }
    }
}