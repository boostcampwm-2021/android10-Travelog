package com.thequietz.travelog.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordBinding

class RecordAdapter : ListAdapter<Record, RecordAdapter.RecordViewHolder>(diffUtil) {
    class RecordViewHolder(private val binding: ItemRecyclerRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val adapter = RecordPhotoAdapter()

        fun bind(item: Record) = with(binding) {
            tvItemRecordTitle.text = item.title
            tvItemRecordSchedule.text = "${item.startDate} ~ ${item.endDate.substring(5)}"
            rvItemRecordPhoto.adapter = adapter
            adapter.submitList(item.images)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(
            ItemRecyclerRecordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val item = currentList[position]

        holder.bind(item)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem == newItem
            }
        }
    }
}
