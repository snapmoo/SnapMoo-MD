package com.bangkit.snapmoo.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.databinding.FragmentReportBinding
import com.bangkit.snapmoo.ui.adapter.ListReportAdapter
import com.bangkit.snapmoo.ui.MainViewModelFactory

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val reportViewModel by viewModels<ReportViewModel> {
        MainViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menyembunyikan AppBar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        setupAction()
    }

    private fun setupAction() {
        reportViewModel.getSession().observe(viewLifecycleOwner) { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                reportViewModel.getAllReport(token).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val data = result.data.data
                            val layoutManager = LinearLayoutManager(activity)
                            binding.rvListReport.layoutManager = layoutManager
                            val adapter = ListReportAdapter()
                            adapter.submitList(data)
                            binding.rvListReport.adapter = adapter
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

    private fun showToast(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvListReport.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
}