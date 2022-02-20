package com.thequietz.travelog.ui.record.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerRecordPhotoBinding
import com.thequietz.travelog.ui.record.model.Record

class RecordPhotoAdapter(
    private val navigateToRecordBasicUi: ((Int, String, String, String) -> Unit),
    private val recordItem: Record
) : ListAdapter<String, RecordPhotoAdapter.RecordPhotoViewHolder>(diffUtil) {
    inner class RecordPhotoViewHolder(private val binding: ItemRecyclerRecordPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) = with(binding) {
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
                Glide.with(itemView)
                    .load(url)
                    .placeholder(R.drawable.bg_photo_placeholder)
                    .into(ivItemRecordPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordPhotoViewHolder {
        return RecordPhotoViewHolder(
            ItemRecyclerRecordPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordPhotoViewHolder, position: Int) {
        val imageUrl = currentList.getOrNull(position) ?: return
        holder.bind(imageUrl)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}
