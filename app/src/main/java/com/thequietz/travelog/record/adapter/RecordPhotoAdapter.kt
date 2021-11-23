package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerRecordPhotoBinding

class RecordPhotoAdapter(
    private val navigateToRecordViewUi: ((Int) -> Unit)? = null,
    private val addImage: (() -> Unit)? = null
) : ListAdapter<String, RecordPhotoAdapter.RecordPhotoViewHolder>(diffUtil) {
    inner class RecordPhotoViewHolder(private val binding: ItemRecyclerRecordPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String?) = with(binding) {
            ivItemRecordPhoto.setOnClickListener {
                if (url == null) {
                    addImage?.invoke()
                }
                navigateToRecordViewUi?.invoke(absoluteAdapterPosition)
                println(absoluteAdapterPosition)
                println(bindingAdapterPosition)
                println(layoutPosition)
            }

            if (url != null) {
                Glide.with(itemView)
                    .load(url)
                    .placeholder(R.drawable.bg_photo_placeholder)
                    .into(ivItemRecordPhoto)
            } else {
                ivItemRecordPhoto.setImageResource(R.drawable.ic_add)
            }
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
        val imageUrl = currentList.getOrNull(position)

        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        if (addImage != null) return super.getItemCount() + 1
        return super.getItemCount()
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
