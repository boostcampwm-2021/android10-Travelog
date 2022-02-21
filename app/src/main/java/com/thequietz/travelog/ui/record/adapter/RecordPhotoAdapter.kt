package com.thequietz.travelog.ui.record.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.common.BaseListAdapter
import com.thequietz.travelog.databinding.ItemRecyclerRecordPhotoBinding
import com.thequietz.travelog.ui.record.model.Record

class RecordPhotoAdapter(
    private val navigateToRecordBasicUi: ((Int, String, String, String) -> Unit),
    private val recordItem: Record
) : BaseListAdapter<String, ItemRecyclerRecordPhotoBinding>(RecordPhotoDiffCallback()) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemRecyclerRecordPhotoBinding {
        return ItemRecyclerRecordPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: ItemRecyclerRecordPhotoBinding, position: Int): Unit = with(binding) {
        val url = currentList[position]
        ivItemRecordPhoto.setOnClickListener {
            navigateToRecordBasicUi.invoke(
                recordItem.travelId,
                recordItem.title,
                recordItem.startDate,
                recordItem.endDate
            )
        }

        if (url == "empty") {
            cvItemRecordPhoto.visibility = View.GONE
        } else {
            Glide.with(root)
                .load(url)
                .placeholder(R.drawable.bg_photo_placeholder)
                .into(ivItemRecordPhoto)
        }
    }
}

class RecordPhotoDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
