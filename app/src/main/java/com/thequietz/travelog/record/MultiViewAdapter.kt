package com.thequietz.travelog.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerManyDateBinding
import com.thequietz.travelog.databinding.ItemRecyclerManyImagesBinding
import com.thequietz.travelog.databinding.ItemRecyclerManyPlaceBinding
import com.thequietz.travelog.databinding.ItemRecyclerRecyclerBinding

class MultiViewAdapter : ListAdapter<MyRecord, RecyclerView.ViewHolder>(
    diffUtil
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (ViewType.values().get(viewType)) {
            ViewType.DATE -> {
                val bindingSchedule = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_recycler_many_date,
                    parent,
                    false
                ) as ItemRecyclerManyDateBinding
                RecordScheduleViewHolder(bindingSchedule)
            }
            ViewType.PLACE -> {
                val bindingPlace = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_recycler_many_place,
                    parent,
                    false
                ) as ItemRecyclerManyPlaceBinding
                RecordPlaceViewHolder(bindingPlace)
            }
            ViewType.IMAGE -> {
                val bindingImage = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_recycler_recycler,
                    parent,
                    false
                ) as ItemRecyclerRecyclerBinding
                RecordImageViewHolder(bindingImage)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyRecord.RecordSchedule -> 0
            is MyRecord.RecordPlace -> 1
            is MyRecord.RecordImageList -> 2
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)) {
            is MyRecord.RecordSchedule -> {
                (holder as RecordScheduleViewHolder).bind(getItem(position) as MyRecord.RecordSchedule)
            }
            is MyRecord.RecordPlace -> {
                (holder as RecordPlaceViewHolder).bind(getItem(position) as MyRecord.RecordPlace)
            }
            is MyRecord.RecordImageList -> {
                (holder as RecordImageViewHolder).bind(getItem(position) as MyRecord.RecordImageList)
            }
        }
    }
    class RecordScheduleViewHolder(val binding: ItemRecyclerManyDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyRecord.RecordSchedule) {
            binding.scheduleItem = item
        }
    }
    class RecordPlaceViewHolder(val binding: ItemRecyclerManyPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyRecord.RecordPlace) {
            binding.placeItem = item
        }
    }
    class RecordImageViewHolder(
        val binding: ItemRecyclerRecyclerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val adapter = MultiViewImageAdapter()
        init {
            binding.rvItemManyImage.adapter = adapter
        }
        fun bind(imageList: MyRecord.RecordImageList) {
            adapter.submitList(imageList.list)
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyRecord>() {
            override fun areItemsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class MultiViewImageAdapter : ListAdapter<RecordImage, MultiViewImageAdapter.MultiViewImageViewHolder>(
    diffUtil
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewImageViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_many_images,
            parent,
            false
        ) as ItemRecyclerManyImagesBinding
        return MultiViewImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MultiViewImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class MultiViewImageViewHolder(val binding: ItemRecyclerManyImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecordImage) {
            binding.imageItem = item
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecordImage>() {
            override fun areItemsTheSame(
                oldItem: RecordImage,
                newItem: RecordImage
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: RecordImage,
                newItem: RecordImage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
