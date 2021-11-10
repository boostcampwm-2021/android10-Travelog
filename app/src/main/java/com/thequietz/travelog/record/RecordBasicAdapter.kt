package com.thequietz.travelog.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicBinding

class RecordBasicAdapter :
    ListAdapter<TravelDestination, RecordBasicAdapter.RecordBasicViewHolder>(diffUtil) {
    class RecordBasicViewHolder(private val binding: ItemRecyclerRecordBasicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val adapter = RecordPhotoAdapter()

        fun bind(item: TravelDestination) = with(binding) {
            tvItemRecordBasicTitle.text = item.name
            btnItemRecordBasicMore
            rvItemRecordBasic.adapter = adapter
            adapter.submitList(item.images)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordBasicViewHolder {
        return RecordBasicViewHolder(
            ItemRecyclerRecordBasicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordBasicViewHolder, position: Int) {
        val item = currentList[position]

        holder.bind(item)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<TravelDestination>() {
            override fun areItemsTheSame(
                oldItem: TravelDestination,
                newItem: TravelDestination
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(
                oldItem: TravelDestination,
                newItem: TravelDestination
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
