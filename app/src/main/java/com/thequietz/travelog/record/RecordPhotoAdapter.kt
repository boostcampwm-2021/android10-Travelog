package com.thequietz.travelog.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerRecordPhotoBinding

class RecordPhotoAdapter : ListAdapter<String, RecordPhotoAdapter.RecordPhotoViewHolder>(diffUtil) {
    class RecordPhotoViewHolder(private val binding: ItemRecyclerRecordPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {
            Glide.with(itemView)
                .load(url)
                .fallback(R.drawable.bg_photo_fallback)
                .into(binding.ivItemRecordPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordPhotoViewHolder {
        return RecordPhotoViewHolder(
            ItemRecyclerRecordPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordPhotoViewHolder, position: Int) {
        val imageUrl = currentList[position]

        holder.bind(imageUrl)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}
