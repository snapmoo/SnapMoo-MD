package com.bangkit.snapmoo.ui.profile.edit_profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.databinding.ActivityEditProfileBinding
import com.bangkit.snapmoo.ui.MainActivity
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.utils.reduceFileImage
import com.bangkit.snapmoo.utils.uriToFile
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditProfileBinding
    private val editProfileViewModel by viewModels<EditProfileViewModel> {
        MainViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setData()
        binding.ivEditPhoto.setOnClickListener { startGallery() }
        binding.btnReset.setOnClickListener { setData() }
        binding.btnSave.setOnClickListener { editProfile() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            startCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCrop(uri: Uri) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val outputFile = File(cacheDir, fileName)
//        Log.d("Lokasi hasil ucrop", outputFile.toString())
        val destinationUri = Uri.fromFile(outputFile)

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 600)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                Log.d("CurrentImageUri", resultUri.toString())
                Glide.with(this)
                    .load(currentImageUri)
                    .into(binding.ivPhotoProfile)
            } else {
                showToast(getString(R.string.cropped_image_uri_is_null))
            }
        }
    }

    private fun setData() {
        editProfileViewModel.getSession().observe(this) { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                editProfileViewModel.getUserData(token).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val data = result.data.data
                            if (data.photo != null) {
                                Glide.with(this@EditProfileActivity)
                                    .load(data.photo)
                                    .into(binding.ivPhotoProfile)
                            } else {
                                binding.ivPhotoProfile.setImageResource(R.drawable.photo_profile)
                            }
                            binding.apply {
                                nameEditText.setText(data.name)
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
    }

    private fun editProfile() {

        var imageFile: File? = null

        currentImageUri?.let { uri ->
            imageFile = uriToFile(uri, this).reduceFileImage()
        } ?: run {
            imageFile = null
        }

        val name = binding.nameEditText.text.toString().trim()
        val phoneNumber = binding.phoneNumberEditText.text.toString().trim()

        if (name.isEmpty()) {
            binding.nameEditText.error = getString(R.string.name_cannot_be_empty)
            return
        }
        if (phoneNumber.isEmpty()) {
            binding.phoneNumberEditText.error = getString(R.string.phonenumber_cannot_be_empty)
            return
        }


        editProfileViewModel.getSession().observe(this) { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                editProfileViewModel.editUserData(
                    token,
                    name,
                    phoneNumber,
                    imageFile
                ).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }

                            is Result.Success -> {
                                showLoading(false)
                                showToast("edit data sukses")
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("openFragment", "profileFragment")
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }


                            is Result.Error -> {
                                showLoading(false)
                                showToast("edit data gagal")
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