package com.bangkit.snapmoo.ui.scan.send_report

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.databinding.ActivitySendReportBinding
import com.bangkit.snapmoo.ui.MainActivity
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.utils.reduceFileImage
import com.bangkit.snapmoo.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SendReportActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySendReportBinding
    private val sendReportViewModel by viewModels<SendReportViewModel> {
        MainViewModelFactory.getInstance(this)
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentImageUri: Uri? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var percentage: Int = 0
    private var indication: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        currentImageUri = Uri.parse(intent.getStringExtra("image_uri"))
        percentage = intent.getIntExtra("percentage", 0)
        indication = intent.getStringExtra("indication") ?: ""

        setData()
        setToolbar()
        setupButtonAction()
    }

    private fun setData() {
        binding.apply {
            ivImageScan.setImageURI(currentImageUri)
            tvScore.text = "${percentage}%"
            tvClasifyResult.text = indication
        }
    }

    private fun setupButtonAction() {
        binding.btnSend.setOnClickListener { uploadData() }
        binding.checkboxShareLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                latitude = null
                longitude = null
            }
        }
        binding.checkboxUseData.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sendReportViewModel.getSession().observe(this) { user ->
                    val token = user.token
                    if (token.isNotEmpty()) {
                        sendReportViewModel.getUserData(token).observe(this) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }

                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data.data
                                    binding.apply {
                                        nameEditText.setText(data.name)
                                        emailEditText.setText(data.email)
                                        phoneNumberEditText.setText(data.phoneNumber)
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
            } else {
                binding.apply {
                    nameEditText.setText("")
                    emailEditText.setText("")
                    phoneNumberEditText.setText("")
                }
            }
        }
    }

    private fun uploadData() {
        currentImageUri?.let { uri ->

            val imageFile = uriToFile(uri, this).reduceFileImage()
            val name = binding.nameEditText.text.toString().trim()
            val phoneNumber = binding.phoneNumberEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val address = binding.addressEditText.text.toString().trim()
            val city = binding.cityEditText.text.toString().trim()
            val province = binding.provinceEditText.text.toString().trim()
            val checkManyHave = binding.haveEditText.text.toString().trim()
            val condition = binding.conditionEditText.text.toString().trim()
            val prediction = indication
            val score = percentage

            if (name.isEmpty()) {
                binding.nameEditText.error = getString(R.string.name_cannot_be_empty)
                return
            }
            if (phoneNumber.isEmpty()) {
                binding.phoneNumberEditText.error = getString(R.string.phonenumber_cannot_be_empty)
                return
            }
            if (email.isEmpty()) {
                binding.emailEditText.error = getString(R.string.email_cannot_be_empty)
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditText.error = getString(R.string.invalid_email_format)
                return
            }
            if (address.isEmpty()) {
                binding.addressEditText.error = getString(R.string.address_cannot_be_empty)
                return
            }
            if (city.isEmpty()) {
                binding.cityEditText.error = getString(R.string.city_cannot_be_empty)
                return
            }
            if (province.isEmpty()) {
                binding.provinceEditText.error = getString(R.string.province_cannot_be_empty)
                return
            }

            var manyHave = 0

            if (checkManyHave.isEmpty()) {
                val number = checkManyHave.toIntOrNull()
                if (number == null) {
                    binding.haveEditText.error =
                        getString(R.string.please_fill_in_the_number_of_cattle_you_have)
                    return
                } else {
                    manyHave = number
                }
            }

            if (condition.isEmpty()) {
                binding.conditionEditText.error = ""
            }

            sendReportViewModel.getSession().observe(this) { user ->
                val token = user.token
                if (token.isNotEmpty()) {
                    sendReportViewModel.uploadReportData(
                        token,
                        name,
                        phoneNumber,
                        email,
                        address,
                        city,
                        province,
                        latitude,
                        longitude,
                        manyHave,
                        condition,
                        prediction,
                        score,
                        imageFile
                    )
                        .observe(this) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {
                                        showLoading(true)
                                    }

                                    is Result.Success -> {
                                        showLoading(false)
                                        showToast("upload sukses")
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
            }
        } ?: showToast("error")
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }

                else -> {
//                    showToast("Location feature cannot be used!")
                    binding.checkboxShareLocation.isChecked = false
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
//                    Log.d("Location", it.toString())
                    showToast(getString(R.string.location_found))
                    latitude = it.latitude
                    longitude = it.longitude
                } else {
                    showToast(getString(R.string.location_not_found))
                    binding.checkboxShareLocation.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
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