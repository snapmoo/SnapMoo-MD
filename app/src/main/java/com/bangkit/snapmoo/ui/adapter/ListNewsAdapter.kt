package com.bangkit.snapmoo.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.snapmoo.data.api.response.NewsResult
import com.bangkit.snapmoo.databinding.ItemRowNewsBinding
import com.bangkit.snapmoo.ui.news.detail_news.DetailNewsActivity
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ListNewsAdapter : ListAdapter<NewsResult, ListNewsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailNewsActivity::class.java)
            intentDetail.putExtra("extra_link", news.link)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: ItemRowNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(items: NewsResult) {
            Glide.with(itemView)
                .load(items.thumbnail)
                .into(binding.ivImgNews)
            Glide.with(itemView)
                .load(items.authorImage)
                .into(binding.ivAuthorLogo)

            val readableDate = convertTimestampToReadableDate(
                items.publishedAt?.seconds,
                items.publishedAt?.nanoseconds
            )

            binding.apply {
                tvTitle.text = items.title
                tvAuthor.text = items.author
                tvPublishAt.text = readableDate
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsResult>() {
            override fun areItemsTheSame(oldItem: NewsResult, newItem: NewsResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NewsResult, newItem: NewsResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}