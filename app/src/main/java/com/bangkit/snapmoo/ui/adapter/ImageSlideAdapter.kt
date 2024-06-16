package com.bangkit.snapmoo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.snapmoo.data.local.ImageData
import com.bangkit.snapmoo.databinding.ItemSlideBinding
import com.bumptech.glide.Glide

class ImageSlideAdapter(private val items: List<ImageData>) :
    RecyclerView.Adapter<ImageSlideAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: ItemSlideBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
        fun bind(data: ImageData) {
            with(binding) {
                Glide.with(itemView)
                    .load(data.imageUrl)
                    .into(ivSlider)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemSlideBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


}