package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.databinding.ItemRecyclerRecordAddImageBinding

class RecordAddImageAdapter :
    ListAdapter<NewRecordImage, RecordAddImageAdapter.RecordAddImageViewHolder>(
        NewRecordImageDiffUtilCallback()
    ) {
    class RecordAddImageViewHolder(val binding: ItemRecyclerRecordAddImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(img: NewRecordImage) {
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

class NewRecordImageDiffUtilCallback : DiffUtil.ItemCallback<NewRecordImage>() {
    override fun areItemsTheSame(oldItem: NewRecordImage, newItem: NewRecordImage): Boolean {
        return oldItem.newRecordImageId == newItem.newRecordImageId
    }

    override fun areContentsTheSame(oldItem: NewRecordImage, newItem: NewRecordImage): Boolean {
        return oldItem == newItem
    }
}
