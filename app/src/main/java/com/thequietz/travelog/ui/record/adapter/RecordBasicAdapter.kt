package com.thequietz.travelog.ui.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.thequietz.travelog.common.BaseListAdapter
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicBinding
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicHeaderBinding
import com.thequietz.travelog.ui.record.model.RecordBasicItem

class RecordBasicAdapter(
    private val navigateToRecordViewUi: (String, String) -> Unit,
    private val updateTargetList: (String, String) -> Unit,
    private val scrollToPosition: (Int) -> Unit
) : BaseListAdapter<RecordBasicItem, ViewDataBinding>(RecordBasicDiffUtil()) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return when (viewType) {
            HEADER_TYPE -> {
                ItemRecyclerRecordBasicHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
            else -> {
                ItemRecyclerRecordBasicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
        }
    }

    override fun bind(binding: ViewDataBinding, position: Int) = with(binding) {
        val item = currentList[position]
        when (this) {
            is ItemRecyclerRecordBasicHeaderBinding -> {
                item as RecordBasicItem.RecordBasicHeader
                root.setOnClickListener {
                    updateTargetList(item.day, item.date)
                    scrollToPosition(position)
                }
                day = item.day
                date = item.date
            }
            is ItemRecyclerRecordBasicBinding -> {
                item as RecordBasicItem.TravelDestination
                root.setOnClickListener {
                    navigateToRecordViewUi.invoke(item.day, item.name)
                }
                seq = item.seq.toString()
                name = item.name
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is RecordBasicItem.RecordBasicHeader -> HEADER_TYPE
            else -> ITEM_TYPE
        }
    }

    fun getPositionOfHeaderFromDay(day: String): Int {
        for ((position, item) in currentList.withIndex()) {
            if (item is RecordBasicItem.RecordBasicHeader && item.day == day) {
                return position
            }
        }
        return -1
    }

    fun getDay(position: Int): String {
        return when (val item = currentList[position]) {
            is RecordBasicItem.RecordBasicHeader -> item.day
            is RecordBasicItem.TravelDestination -> item.day
        }
    }

    fun getDate(position: Int): String {
        return when (val item = currentList[position]) {
            is RecordBasicItem.RecordBasicHeader -> item.date
            is RecordBasicItem.TravelDestination -> item.date
        }
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
