package com.bangkit.snapmoo.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.databinding.FragmentProfileBinding
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.ui.profile.bookmark.BookmarkActivity
import com.bangkit.snapmoo.ui.profile.edit_profile.EditProfileActivity
import com.bangkit.snapmoo.ui.profile.history.HistoryActivity
import com.bangkit.snapmoo.ui.profile.setting.SettingActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bangkit.snapmoo.data.Result

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel> {
        MainViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menyembunyikan AppBar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                profileViewModel.getUserData(token).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                        }

                        is Result.Success -> {
                            val data = result.data.data
                            if (data.photo != null) {
                                Glide.with(requireActivity())
                                    .load(data.photo)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.civPhotoProfile)
                            } else {
                                binding.civPhotoProfile.setImageResource(R.drawable.photo_profile)
                            }
                            binding.tvUsername.text = data.name
                            binding.tvGmail.text = data.email
                        }

                        is Result.Error -> {
                            showToast(result.error)
                        }
                    }
                }
            }
        }
        setupAction()
    }

    private fun setupAction() {
        binding.apply {

            btnSetting.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, SettingActivity::class.java))
                }
            }
            btnEditProfile.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, EditProfileActivity::class.java))
                }
            }
            viewHistory.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, HistoryActivity::class.java))
                }
            }
            viewBookmark.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, BookmarkActivity::class.java))
                }
            }

        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

}