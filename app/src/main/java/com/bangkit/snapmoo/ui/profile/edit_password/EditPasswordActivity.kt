package com.bangkit.snapmoo.ui.profile.edit_password

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.databinding.ActivityEditPasswordBinding

class EditPasswordActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
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
            title.toString()  // default title
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