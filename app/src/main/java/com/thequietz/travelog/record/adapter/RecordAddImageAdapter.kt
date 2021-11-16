package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordAddImageBinding
import com.thequietz.travelog.record.viewmodel.NewImage

class RecordAddImageAdapter : ListAdapter<NewImage, RecordAddImageAdapter.RecordAddImageViewHolder>(
    NewImageDiffUtilCallback()
) {
    class RecordAddImageViewHolder(val binding: ItemRecyclerRecordAddImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(img: NewImage) {
            binding.item = img
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordAddImageViewHolder {
        return RecordAddImageViewHolder(
            ItemRecyclerRecordAddImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordAddImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class NewImageDiffUtilCallback : DiffUtil.ItemCallback<NewImage>() {
    override fun areItemsTheSame(oldItem: NewImage, newItem: NewImage): Boolean {
        return oldItem.bitmap == newItem.bitmap
    }

    override fun areContentsTheSame(oldItem: NewImage, newItem: NewImage): Boolean {
        return oldItem == newItem
    }
}
