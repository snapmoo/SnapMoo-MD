package com.bangkit.snapmoo.ui.profile.bookmark

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.Result
import com.bangkit.snapmoo.databinding.ActivityBookmarkBinding
import com.bangkit.snapmoo.ui.adapter.ListBookmarkAdapter
import com.bangkit.snapmoo.ui.MainViewModelFactory

class BookmarkActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityBookmarkBinding
    private val bookmarkViewModel by viewModels<BookmarkViewModel> {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setToolbar()
    }

    private fun setupAction() {
        bookmarkViewModel.getSession().observe(this) { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                bookmarkViewModel.getSavedHistory(token).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val data = result.data.data
                            val layoutManager = LinearLayoutManager(this)
                            binding.rvListBookmark.layoutManager = layoutManager
                            val adapter = ListBookmarkAdapter()
                            adapter.submitList(data)
                            binding.rvListBookmark.adapter = adapter

                            adapter.onClick = {
                                val checkIsSaved = !(it.isSaved)
                                AlertDialog.Builder(this@BookmarkActivity).apply {
                                    setTitle("Bookmark history")
                                    setMessage("Are you sure you want to remove bookmark for this history?")
                                    setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                                        deleteFromBookmark(token, it.historyId, checkIsSaved)
                                    }
                                    setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, _: Int ->
                                        dialogInterface.dismiss()
                                    }
                                    create()
                                    show()
                                }
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

    private fun deleteFromBookmark(token: String, historyId: String, isSaved: Boolean) {
        bookmarkViewModel.deleteBookmark(token, historyId, isSaved)
            .observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                    }

                    is Result.Success -> {
                        showToast("Data berhasil dihapus")
                        setupAction()
                    }

                    is Result.Error -> {
                    }

                }
            }
    }


    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvListBookmark.visibility = if (isLoading) View.GONE else View.VISIBLE
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