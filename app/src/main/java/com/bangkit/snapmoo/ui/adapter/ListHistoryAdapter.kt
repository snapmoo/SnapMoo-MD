package com.bangkit.snapmoo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.api.response.HistoryResult
import com.bangkit.snapmoo.databinding.ItemRowHistoryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ListHistoryAdapter :
    ListAdapter<HistoryResult, ListHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    var onClick: ((HistoryResult) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClick)
    }

    class MyViewHolder(private val binding: ItemRowHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(items: HistoryResult, onClick: ((HistoryResult) -> Unit)?) {
            Glide.with(itemView)
                .load(items.photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageItemScan)

            val readableDate = convertTimestampToReadableDate(
                items.createdAt.seconds,
                items.createdAt.nanoseconds
            )

            binding.apply {
//                tvScan.text = items.name
                tvIdHistory.text = items.historyId
                tvClassifyResult.text = "${items.result} : ${items.score}%"
                tvDateHistory.text = readableDate
                val showButton = items.isSaved
                if (showButton) {
                    addToBookmark.visibility = View.GONE
                } else {
                    addToBookmark.visibility = View.VISIBLE
                }
                addToBookmark.setOnClickListener {
                    onClick?.invoke(items)
                }
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
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResult>() {
            override fun areItemsTheSame(oldItem: HistoryResult, newItem: HistoryResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryResult,
                newItem: HistoryResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}