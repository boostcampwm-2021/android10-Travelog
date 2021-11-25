package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordBinding
import com.thequietz.travelog.record.model.Record

class RecordAdapter(
    private val navigateToRecordBasicUi: (Int, String, String, String) -> Unit
) : ListAdapter<Record, RecordAdapter.RecordViewHolder>(diffUtil) {
    inner class RecordViewHolder(private val binding: ItemRecyclerRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val layoutManager = object : LinearLayoutManager(itemView.context) {
            override fun canScrollHorizontally(): Boolean = false
        }

        fun bind(item: Record) = with(binding) {
            root.setOnClickListener {
                navigateToRecordBasicUi.invoke(
                    item.travelId,
                    item.title,
                    item.startDate,
                    item.endDate
                )
            }
            tvItemRecordTitle.text = item.title
            tvItemRecordSchedule.text = "${item.startDate} ~ ${item.endDate.substring(5)}"
            val adapter =
                RecordPhotoAdapter(navigateToRecordBasicUi = navigateToRecordBasicUi, recordItem = item)
            rvItemRecordPhoto.adapter = adapter
            rvItemRecordPhoto.layoutManager = layoutManager
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
