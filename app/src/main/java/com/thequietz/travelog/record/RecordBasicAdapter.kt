package com.thequietz.travelog.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicBinding
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicHeaderBinding

sealed class RecordBasicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class RecordBasicHeaderViewHolder(private val binding: ItemRecyclerRecordBasicHeaderBinding) :
        RecordBasicViewHolder(binding.root) {
        init {
            binding.btnItemRecordBasicHeaderEdit.setOnClickListener {
                // TODO: 수정 버튼 클릭 이벤트
            }
        }

        override fun <T : RecordBasicItem> bind(item: T) {
            item as RecordBasicItem.RecordBasicHeader
            binding.tvItemRecordBasicHeaderTitle.text = item.day
        }
    }

    class RecordBasicItemViewHolder(private val binding: ItemRecyclerRecordBasicBinding) :
        RecordBasicViewHolder(binding.root) {
        private val adapter = RecordPhotoAdapter()

        init {
            binding.btnItemRecordBasicMore.setOnClickListener {
                // TODO: 더보기 버튼 클릭 이벤트
            }
        }

        override fun <T : RecordBasicItem> bind(item: T) = with(binding) {
            item as RecordBasicItem.TravelDestination
            tvItemRecordBasicTitle.text = item.name
            rvItemRecordBasic.adapter = adapter
            adapter.submitList(item.images)
        }
    }

    abstract fun <T : RecordBasicItem> bind(item: T)
}

class RecordBasicAdapter :
    ListAdapter<RecordBasicItem, RecordBasicViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is RecordBasicItem.RecordBasicHeader -> HEADER_TYPE
            else -> ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordBasicViewHolder {
        return when (viewType) {
            HEADER_TYPE -> {
                RecordBasicViewHolder.RecordBasicHeaderViewHolder(
                    ItemRecyclerRecordBasicHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                RecordBasicViewHolder.RecordBasicItemViewHolder(
                    ItemRecyclerRecordBasicBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecordBasicViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private const val HEADER_TYPE = 0
        private const val ITEM_TYPE = 1

        private val diffUtil = object : DiffUtil.ItemCallback<RecordBasicItem>() {
            override fun areItemsTheSame(
                oldItem: RecordBasicItem,
                newItem: RecordBasicItem
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(
                oldItem: RecordBasicItem,
                newItem: RecordBasicItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
