package com.thequietz.travelog.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordBinding

class RecordAdapter : ListAdapter<Record, RecordAdapter.RecordViewHolder>(diffUtil) {
    class RecordViewHolder(private val binding: ItemRecyclerRecordBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(
            ItemRecyclerRecordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
                TODO("Not yet implemented")
            }
        }
    }
}
