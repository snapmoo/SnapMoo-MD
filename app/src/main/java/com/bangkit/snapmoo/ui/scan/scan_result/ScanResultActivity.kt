package com.bangkit.snapmoo.ui.scan.scan_result

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.databinding.ActivityScanResultBinding
import com.bangkit.snapmoo.ml.Model
import com.bangkit.snapmoo.ui.scan.send_report.SendReportActivity
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.utils.reduceFileImage
import com.bangkit.snapmoo.utils.uriToBitmap
import com.bangkit.snapmoo.utils.uriToFile
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.roundToInt

class ScanResultActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityScanResultBinding
    private var currentImageUri: Uri? = null
    private var percentage: Int = 0
    private var indication: String = ""
    private val scanResultViewModel by viewModels<ScanResultViewModel> {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentImageUri = Uri.parse(intent.getStringExtra("image_uri"))
        // Reset status upload jika gambar baru di-scan
        val sharedPref = getSharedPreferences("scan_result", MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove(currentImageUri.toString())
            apply()
        }

        setToolbar()
        postPrediction()
        setButtonAction()
    }

    private fun preprocessImage(bitmap: Bitmap): TensorBuffer {
        val imageSize = 224
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(imageSize * imageSize)
        resizedBitmap.getPixels(
            intValues,
            0,
            resizedBitmap.width,
            0,
            0,
            resizedBitmap.width,
            resizedBitmap.height
        )

        var pixel = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val value = intValues[pixel++]

                byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value and 0xFF) / 255.0f))
            }
        }

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)
        return inputFeature0
    }


    private fun postPrediction() {
        currentImageUri?.let { uri ->
            val model = Model.newInstance(this)
            val bitmap = uriToBitmap(this, uri)
            if (bitmap != null) {
                val input = preprocessImage(bitmap)
                val outputFeature0 = model.process(input).outputFeature0AsTensorBuffer
                val result = outputFeature0.floatArray
//                Log.d("Prediction result:", result[0].toString())
                val confidenceScore = result[0] * 100
                val roundedNumber = confidenceScore.roundToInt()
                var temp = 0

                if (roundedNumber > 50) {
                    indication = getString(R.string.positive)
                    binding.tvIndicateScanResult.text = getString(R.string.positive)
                    binding.tvStepHandling.text = """
1. Lakukan isolasi terhadap sapi yang terinfeksi PMK supaya tidak menularkan virus ke hewan ternak lainnya
2. Berikan vitamin dan suplemen ATP
3. Berikan Antipiretik (mengurangi suhu tubuh) dan Analgesik (mengurangi nyeri)
4. Upayakan sapi yang terinfeksi untuk tetap makan meskipun nafsu makan menurun
5. Pemberian obat dan vitamin dilakukan berulang hingga sapi sembuh
                    """.trimIndent()
                    binding.tvDescription.text = """
                        Oops! Sapi anda terdeteksi PMK 😥
                        Berdasarkan gambar yang anda upload, bagian sapi tersebut terdeteksi Penyakit Mulut dan Kuku (PMK) nih. Anda bisa lakukan beberapa tips di bawah ini untuk penanganan yang harus dilakukan supaya penyakit tersebut tidak semakin menyebar.
                    """.trimIndent()
                    binding.tvAdditionalDescription.text = """
                        Jangan panik dan khawatir! Anda bisa lakukan penanganan di atas kemudian lakukan pemeliharan berkala dan melakukan pengecekan kondisi sapi dengan memanfaatkan aplikasi snapmoo!
                    """.trimIndent()
                    temp = roundedNumber

                } else {
                    indication = getString(R.string.negative)
                    binding.tvIndicateScanResult.text = getString(R.string.negative)
                    binding.tvStepHandling.text = """
1. Menjaga kualitas pakan
2. Menjaga kondisi kebersihan lingkungan kandang
3. Menjaga daya tahan tubuh ternak
                    """.trimIndent()
                    binding.tvDescription.text = """
Wahh, sapi kamu tidak terinfeksi PMK!
Yukk terapkan beberapa saran ini untuk mempertahankan Kesehatan sapi dan terhindar dari PMK!
                    """.trimIndent()
                    binding.tvAdditionalDescription.text = """
                        Lakukan pemeliharaan kesehatan sapi anda dengan rutin memeriksa kondisi sapi. Anda bisa memanfaatkan fitur scan pada aplikasi snapmoo secara berkala loh!
                        """.trimIndent()
                    temp = 100 - roundedNumber
                }
                percentage = temp
                binding.tvScoreScan.text = "${temp}%"
                binding.ivImageScan.setImageURI(uri)

                postHistory()

            } else {
                Log.e("Error", "Failed to load image from URI")
            }
            model.close()
        } ?: showToast(getString(R.string.predict_failed))
    }

    private fun postHistory() {
        currentImageUri?.let { uri ->

            val sharedPref = getSharedPreferences("scan_result", MODE_PRIVATE)
            val alreadyUploaded = sharedPref.getBoolean(uri.toString(), false)

            if (alreadyUploaded) {
                Log.d("postHistory", "Data already uploaded, skipping upload.")
                return
            }

            val imageFile = uriToFile(uri, this).reduceFileImage()

            scanResultViewModel.getSession().observe(this) { user ->
                val token = user.token
                if (token.isNotEmpty()) {
                    scanResultViewModel.postHistory(
                        token,
                        indication,
                        percentage,
                        imageFile
                    ).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }

                                is Result.Success -> {
                                    showLoading(false)
                                    showToast("upload ke history sukses")
                                    // Simpan status upload ke SharedPreferences
                                    with(sharedPref.edit()) {
                                        putBoolean(uri.toString(), true)
                                        apply()
                                    }
                                }

                                is Result.Error -> {
                                    showLoading(false)
                                    showToast("upload ke history gagal")
                                    showToast(result.error)
                                }
                            }
                        }
                    }
                }
            }
        } ?: showToast("error")
    }

    private fun setButtonAction() {
        binding.btnRequest.setOnClickListener {
            val intent = Intent(this, SendReportActivity::class.java).apply {
                putExtra("image_uri", currentImageUri.toString())
                putExtra("indication", indication)
                putExtra("percentage", percentage)
            }
            startActivity(intent)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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