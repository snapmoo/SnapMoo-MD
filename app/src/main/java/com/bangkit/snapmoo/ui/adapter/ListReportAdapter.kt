package com.bangkit.snapmoo.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.snapmoo.data.api.response.ReportResult

import com.bangkit.snapmoo.databinding.ItemRowReportBinding
import com.bangkit.snapmoo.ui.report.detail.DetailReportActivity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class  ListReportAdapter : ListAdapter<ReportResult, ListReportAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val report = getItem(position)
        holder.bind(report)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailReportActivity::class.java)
            intentDetail.putExtra("extra_id", report.id)
            holder.itemView.context.startActivity(intentDetail)

        }
    }

    class MyViewHolder(private val binding: ItemRowReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(items: ReportResult) {
//            Glide.with(itemView)
//                .load(items.photo)
//                .into(binding.ivPhoto) belum ada ivphoto

            val readableDate = convertTimestampToReadableDate(
                items.createdAt?.seconds,
                items.createdAt?.nanoseconds
            )

            binding.apply {
                tvUsername.text = items.name
                tvClasifyResult.text = "${items.prediction.toString()} : ${items.score.toString()}%"
                tvIdReport.text = items.id
                tvDateReport.text = readableDate
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReportResult>() {
            override fun areItemsTheSame(oldItem: ReportResult, newItem: ReportResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ReportResult, newItem: ReportResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}