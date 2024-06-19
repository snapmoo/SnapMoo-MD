package com.bangkit.snapmoo.ui.news

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.data.api.response.NewsResult
import com.bangkit.snapmoo.databinding.FragmentNewsBinding
import com.bangkit.snapmoo.ui.MainViewModelFactory
import com.bangkit.snapmoo.ui.adapter.ListNewsAdapter

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val newsViewModel by viewModels<NewsViewModel> {
        MainViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menyembunyikan AppBar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        setupAction()
    }

//    private fun setupAction() {
//        newsViewModel.getSession().observe(viewLifecycleOwner) { user ->
//            val token = user.token
//            if (token.isNotEmpty()) {
//                newsViewModel.getAllNews(token).observe(viewLifecycleOwner) { result ->
//                    when (result) {
//                        is Result.Loading -> {
//                            showLoading(true)
//                        }
//
//                        is Result.Success -> {
//                            showLoading(false)
//                            val data = result.data.data
//                            val layoutManager = LinearLayoutManager(activity)
//                            binding.rvNews.layoutManager = layoutManager
//                            val adapter = ListNewsAdapter()
//                            adapter.submitList(data)
//                            binding.rvNews.adapter = adapter
//                        }
//
//                        is Result.Error -> {
//                            showLoading(false)
//                            showToast(result.error)
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun setupAction() {
        newsViewModel.getSession().observe(viewLifecycleOwner) { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                // Set TextWatcher to listen for search query changes
                binding.searchView.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val query = s.toString()
                        searchNews(token, query)
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                // Initial load of news without query
                searchNews(token, "")
            }
        }
    }

    private fun searchNews(token: String, query: String) {
        var data: List<NewsResult>
        newsViewModel.searchNews(token, query).observe(viewLifecycleOwner) { result ->
            when (result) {


                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    data = result.data.data
                    binding.noDataFound.visibility = View.GONE
                    binding.rvNews.visibility = View.VISIBLE
                    val layoutManager = LinearLayoutManager(activity)
                    binding.rvNews.layoutManager = layoutManager
                    val adapter = ListNewsAdapter()
                    adapter.submitList(data)
                    binding.rvNews.adapter = adapter
                }

                is Result.Error -> {
                    showLoading(false)
                    if (result.error.isNotEmpty()) {
                        binding.noDataFound.visibility = View.VISIBLE
                        binding.rvNews.visibility = View.GONE
                    }
//                    showToast(result.error)
                }
            }
        }
    }

//    private fun showToast(message: String?) {
//        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        binding.rvNews.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
}