package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.data.db.dao.JoinRecord
import com.thequietz.travelog.databinding.ItemViewpagerImageBinding
import com.thequietz.travelog.record.model.RecordImage

class ImageViewPagerAdapter : ListAdapter<JoinRecord, ImageViewPagerAdapter.ViewPagerImageViewHolder>(
    JoinRecordDiffUtilCallback()
) {
    class ViewPagerImageViewHolder(val binding: ItemViewpagerImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JoinRecord) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerImageViewHolder {
        return ViewPagerImageViewHolder(
            ItemViewpagerImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPagerImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class RecordImageDiffUtilCallback : DiffUtil.ItemCallback<RecordImage>() {
    override fun areItemsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
        return oldItem == newItem
    }
}
class JoinRecordDiffUtilCallback : DiffUtil.ItemCallback<JoinRecord>() {
    override fun areItemsTheSame(oldItem: JoinRecord, newItem: JoinRecord): Boolean {
        return oldItem.newRecordImage.newRecordImageId == newItem.newRecordImage.newRecordImageId
    }

    override fun areContentsTheSame(oldItem: JoinRecord, newItem: JoinRecord): Boolean {
        return oldItem == newItem
    }
}
