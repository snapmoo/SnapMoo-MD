package com.bangkit.snapmoo.ui.report.detail

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.databinding.ActivityDetailReportBinding
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bumptech.glide.Glide

class DetailReportActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailReportBinding
    private var reportId: String = ""
    private val detailReportViewModel by viewModels<DetailReportViewModel> {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reportId = intent.getStringExtra("extra_id").toString()
        setToolbar()
        setData()
    }

    private fun setToolbar() {
        binding.toolbar.btnBackToolbar.setOnClickListener {
            onBackPressed()
        }
        val label = getActivityLabel()
        binding.toolbar.tvToolbarTitle.text = label
    }

    private fun setData() {
        detailReportViewModel.getSession().observe(this) { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                detailReportViewModel.getDetailReport(token, reportId)
                    .observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }

                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data.data
                                    binding.apply {
                                        tvName.text = data.name
                                        tvEmail.text = data.email
                                        tvPhoneNumber.text = data.phoneNumber
                                        tvAddress.text = data.address
                                        tvTotal.text = data.manyHave
                                        tvCondition.text = data.affectedBodyPart
                                        tvClasifyResult.text = data.prediction.toString()
                                        tvScore.text = "${data.score.toString()}%"
                                        Glide.with(this@DetailReportActivity)
                                            .load(data.photo)
                                            .into(ivImageScan)
                                    }
                                }

                                is Result.Error -> {
                                    showLoading(false)
                                    showToast(result.error)
                                }
                            }
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