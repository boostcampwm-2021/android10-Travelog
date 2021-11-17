package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordAddImageBinding
import com.thequietz.travelog.record.model.RecordImage

class RecordAddImageAdapter : ListAdapter<RecordImage, RecordAddImageAdapter.RecordAddImageViewHolder>(
    RecordImageDiffUtilCallback()
) {
    class RecordAddImageViewHolder(val binding: ItemRecyclerRecordAddImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(img: RecordImage) {
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
