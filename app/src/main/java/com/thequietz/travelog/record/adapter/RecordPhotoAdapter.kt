package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerRecordPhotoBinding
import com.thequietz.travelog.record.model.RecordBasicItem

class RecordPhotoAdapter(
    private val navigateToRecordBasicUi: ((Int) -> Unit)? = null,
    private val navigateToRecordViewUi: ((Int, String, Int) -> Unit)? = null,
    private val addImage: (() -> Unit)? = null,
    private val item: RecordBasicItem.TravelDestination? = null,
    private val travelId: Int? = null
) : ListAdapter<String, RecordPhotoAdapter.RecordPhotoViewHolder>(diffUtil) {
    inner class RecordPhotoViewHolder(private val binding: ItemRecyclerRecordPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String?) = with(binding) {
            ivItemRecordPhoto.setOnClickListener {
                // 여행 기록 기본 화면으로 이동 시
                if (travelId != null) {
                    navigateToRecordBasicUi?.invoke(travelId)
                }

                // 여행 기록 상세 화면으로 이동 시
                if (item != null) {
                    navigateToRecordViewUi?.invoke(absoluteAdapterPosition, item.date, item.group)
                }

                // 여행 기록 추가 화면에서 이미지 추가 시
                if (url == null) {
                    addImage?.invoke()
                }
            }

            if (url != null) {
                Glide.with(itemView)
                    .load(url)
                    .placeholder(R.drawable.bg_photo_placeholder)
                    .into(ivItemRecordPhoto)
            } else {
                ivItemRecordPhoto.setImageResource(R.drawable.ic_add)
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
        val imageUrl = currentList.getOrNull(position)

        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        if (addImage != null) return super.getItemCount() + 1
        return super.getItemCount()
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
