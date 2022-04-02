package com.thequietz.travelog.ui.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.thequietz.travelog.common.BaseListAdapter
import com.thequietz.travelog.databinding.ItemRecyclerRecordBinding
import com.thequietz.travelog.ui.record.model.Record

class RecordAdapter(
    private val navigateToRecordBasicUi: (Int, String, String, String) -> Unit
) : BaseListAdapter<Record, ItemRecyclerRecordBinding>(RecordDiffCallback()) {

    private lateinit var adapter: RecordPhotoAdapter

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemRecyclerRecordBinding {
        return ItemRecyclerRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: ItemRecyclerRecordBinding, position: Int) = with(binding) {
        val item = currentList[position]
        root.setOnClickListener {
            navigateToRecordBasicUi(item.travelId, item.title, item.startDate, item.endDate)
        }
        title = item.title
        schedule = "${item.startDate} ~ ${item.endDate.substring(5)}"
        adapter = RecordPhotoAdapter(navigateToRecordBasicUi, item)
        rvItemRecordPhoto.adapter = adapter
        adapter.submitList(item.images)
    }
}

private class RecordDiffCallback : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}
