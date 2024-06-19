package com.bangkit.snapmoo.ui.report.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.data.api.response.DetailReportResult
import com.bangkit.snapmoo.databinding.ActivityDetailReportBinding
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DetailReportActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailReportBinding
    private var reportId: String = ""
    private val detailReportViewModel by viewModels<DetailReportViewModel> {
        MainViewModelFactory.getInstance(this)
    }
    private lateinit var data: DetailReportResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reportId = intent.getStringExtra("extra_id").toString()
        binding.btnSendReport.setOnClickListener {
            sendEmail(data)
        }
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
                                    data = result.data.data
                                    binding.apply {
                                        tvName.text = ": ${data.name}"
                                        tvEmail.text = ": ${data.email}"
                                        tvPhoneNumber.text = ": ${data.phoneNumber}"
                                        tvAddress.text =
                                            ": ${data.address}, ${data.city}, ${data.province}"
                                        tvTotal.text = ": ${data.manyHave}"
                                        tvCondition.text = ": ${data.affectedBodyPart}"
                                        tvClasifyResult.text = data.prediction.toString()
                                        tvScore.text = "${data.score.toString()}%"
                                        val readableDate = convertTimestampToReadableDate(
                                            data.createdAt?.seconds,
                                            data.createdAt?.nanoseconds
                                        )
                                        tvCreatedAt.text =": $readableDate"
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

    private fun sendEmail(data: DetailReportResult) {


        val subject = "Permohonan Bantuan Penanganan Kasus PMK Berdasarkan Laporan Terlampir"
        val url = if (data.latitude != null && data.longitude != null) {
            "https://maps.google.com/?q=${data.latitude},${data.longitude}"
        } else {
            "-"
        }

        val message = """
Dengan hormat,

Kami ingin menyampaikan laporan mengenai adanya kasus PMK yang memerlukan perhatian dan bantuan dari instansi terkait. Adapun data yang telah kami himpun adalah sebagai berikut:

Nama: ${data.name ?: "-"}
Nomor Telepon: ${data.phoneNumber ?: "-"}
Email: ${data.email ?: "-"}
Alamat: ${data.address ?: "-"}
Kota: ${data.city ?: "-"}
Provinsi: ${data.province ?: "-"}
Total hewan ternak: ${data.manyHave ?: "-"}
Deskripsi tambahan: ${data.affectedBodyPart ?: "-"}
Titik lokasi: ${url}
Foto: ${data.photo ?: "-"}
Indikasi: ${data.prediction ?: "-"}
Skor Indikasi: ${data.score ?: "-"}%

Berdasarkan data di atas, kami memohon bantuan dan intervensi dari instansi terkait untuk menangani dan menanggulangi penyebaran penyakit ini. Kami siap berkoordinasi lebih lanjut guna memastikan penanganan yang tepat.

Apabila diperlukan, kami siap memberikan informasi tambahan dan detail lebih lanjut. Kami berharap agar permohonan ini dapat ditindaklanjuti secepatnya.

Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.
"""

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // hanya aplikasi email yang dapat menangani intent ini
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("snapmoo@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertTimestampToReadableDate(seconds: Long?, nanoseconds: Long?): String {
        val validSeconds = seconds ?: 0L
        val validNanoseconds = nanoseconds ?: 0L
        val instant = Instant.ofEpochSecond(validSeconds, validNanoseconds)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
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