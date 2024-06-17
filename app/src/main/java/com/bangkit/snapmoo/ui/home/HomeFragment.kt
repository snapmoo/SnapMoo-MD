package com.bangkit.snapmoo.ui.home

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
import com.bangkit.snapmoo.databinding.FragmentHomeBinding
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.ui.home.foot.FootActivity
import com.bangkit.snapmoo.ui.home.mouth.MouthActivity
import com.bangkit.snapmoo.ui.home.udder.UdderActivity
import com.bangkit.snapmoo.ui.scan.CameraActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bangkit.snapmoo.data.Result


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        MainViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menyembunyikan AppBar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            val token = user.token
//            if (user.photo != "") {
//                Glide.with(requireActivity())
//                    .load(user.photo)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .into(binding.imgPhotoProfile)
//            } else {
//                binding.imgPhotoProfile.setImageResource(R.drawable.photo_profile)
//            }
//            binding.tvGreetingName.text = user.name
//        }

            if (token.isNotEmpty()) {
                homeViewModel.getUserData(token).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                        }

                        is Result.Success -> {
//                            showLoading(false)
                            val data = result.data.data
                            if (data.photo != null) {
                                Glide.with(requireActivity())
                                    .load(data.photo)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.imgPhotoProfile)
                            } else {
                                binding.imgPhotoProfile.setImageResource(R.drawable.photo_profile)
                            }
                            binding.tvGreetingName.text = data.name
                        }

                        is Result.Error -> {
//                            showLoading(false)
                            showToast(result.error)
                        }
                    }

                    setupAction()
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {

            btnIconCamera.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, CameraActivity::class.java))
                }
            }
            btnFoot.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, FootActivity::class.java))
                }
            }
            btnMouth.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, MouthActivity::class.java))
                }
            }
            btnUdder.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, UdderActivity::class.java))
                }
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }

}
