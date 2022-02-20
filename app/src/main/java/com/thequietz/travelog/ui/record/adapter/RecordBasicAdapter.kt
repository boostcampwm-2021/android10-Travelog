package com.thequietz.travelog.ui.record.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicBinding
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicHeaderBinding
import com.thequietz.travelog.ui.record.model.RecordBasicItem

sealed class RecordBasicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class RecordBasicHeaderViewHolder(
        private val binding: ItemRecyclerRecordBasicHeaderBinding,
        private val updateTargetList: (String) -> Unit,
        private val scrollToPosition: (Int) -> Unit
    ) : RecordBasicViewHolder(binding.root) {
        private lateinit var item: RecordBasicItem.RecordBasicHeader

        override fun <T : RecordBasicItem> bind(item: T) = with(binding) {
            this@RecordBasicHeaderViewHolder.item = item as RecordBasicItem.RecordBasicHeader
            root.setOnClickListener {
                updateTargetList(item.day)
                scrollToPosition(absoluteAdapterPosition)
            }
            tvItemRecordBasicHeaderTitle.text = item.day
            tvItemRecordBasicHeaderDate.text = item.date
        }

        override fun getDay(): String = item.day
        override fun getDate(): String = item.date
    }

    class RecordBasicItemViewHolder(
        private val binding: ItemRecyclerRecordBasicBinding,
        private val navigateToRecordViewUi: (String, String) -> Unit
    ) : RecordBasicViewHolder(binding.root) {
        private lateinit var item: RecordBasicItem.TravelDestination

        override fun <T : RecordBasicItem> bind(item: T) = with(binding) {
            this@RecordBasicItemViewHolder.item = item as RecordBasicItem.TravelDestination
            root.setOnClickListener {
                navigateToRecordViewUi.invoke(item.day, item.name)
            }
            tvItemRecordBasicSeq.text = item.seq.toString()
            tvItemRecordBasicTitle.text = item.name
        }

        override fun getDay(): String = item.day
        override fun getDate(): String = item.date
    }

    abstract fun <T : RecordBasicItem> bind(item: T)

    abstract fun getDay(): String

    abstract fun getDate(): String
}

class RecordBasicAdapter(
    private val navigateToRecordViewUi: (String, String) -> Unit,
    private val updateTargetList: (String) -> Unit,
    private val scrollToPosition: (Int) -> Unit
) : ListAdapter<RecordBasicItem, RecordBasicViewHolder>(RecordBasicDiffUtil()) {
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
                    ),
                    updateTargetList,
                    scrollToPosition
                )
            }
            else -> {
                RecordBasicViewHolder.RecordBasicItemViewHolder(
                    ItemRecyclerRecordBasicBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    navigateToRecordViewUi
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecordBasicViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun getPositionOfHeaderFromDay(day: String): Int {
        for ((position, item) in currentList.withIndex()) {
            if (item is RecordBasicItem.RecordBasicHeader && item.day == day) {
                return position
            }
        }
        return -1
    }

    companion object {
        private const val HEADER_TYPE = 0
        private const val ITEM_TYPE = 1
    }
}

class RecordBasicDiffUtil : DiffUtil.ItemCallback<RecordBasicItem>() {
    override fun areItemsTheSame(oldItem: RecordBasicItem, newItem: RecordBasicItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: RecordBasicItem, newItem: RecordBasicItem): Boolean {
        return oldItem == newItem
    }
}
