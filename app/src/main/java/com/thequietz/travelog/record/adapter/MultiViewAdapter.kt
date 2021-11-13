package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordManyDateBinding
import com.thequietz.travelog.databinding.ItemRecyclerRecordManyImagesBinding
import com.thequietz.travelog.databinding.ItemRecyclerRecordManyPlaceBinding
import com.thequietz.travelog.databinding.RecyclerRecordManyImageBinding
import com.thequietz.travelog.record.model.MyRecord
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.record.model.ViewType
import com.thequietz.travelog.record.view.RecordViewManyFragmentDirections

class MultiViewAdapter : ListAdapter<MyRecord, RecyclerView.ViewHolder>(
    diffUtil
) {

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

    class RecordScheduleViewHolder(val binding: ItemRecyclerRecordManyDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyRecord.RecordSchedule) {
            binding.scheduleItem = item
        }
    }

    class RecordPlaceViewHolder(val binding: ItemRecyclerRecordManyPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyRecord.RecordPlace) {
            binding.placeItem = item
        }
    }

    class RecordImageViewHolder(
        val binding: RecyclerRecordManyImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val adapter = MultiViewImageAdapter(object : MultiViewImageAdapter.OnItemClickListener {
            override fun onItemClick(view: View, ind: Int) {
                val action = RecordViewManyFragmentDirections
                    .actionRecordViewManyFragmentToRecordViewOneFragment(ind)
                itemView.findNavController().navigate(action)
            }
        })

        init {
            binding.rvItemManyImage.adapter = adapter
        }

        fun bind(imageList: MyRecord.RecordImageList) {
            adapter.submitList(imageList.list)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (ViewType.values().get(viewType)) {
            ViewType.DATE -> {
                RecordScheduleViewHolder(
                    ItemRecyclerRecordManyDateBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
                /*val bindingSchedule = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_recycler_many_date,
                    parent,
                    false
                ) as ItemRecyclerManyDateBinding
                RecordScheduleViewHolder(bindingSchedule)*/
            }
            ViewType.PLACE -> {
                RecordPlaceViewHolder(
                    ItemRecyclerRecordManyPlaceBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
                /*val bindingPlace = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_recycler_many_place,
                    parent,
                    false
                ) as ItemRecyclerManyPlaceBinding
                RecordPlaceViewHolder(bindingPlace)*/
            }
            ViewType.IMAGE -> {
                RecordImageViewHolder(
                    RecyclerRecordManyImageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
                /*val bindingImage = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_recycler_recycler,
                    parent,
                    false
                ) as ItemRecyclerRecyclerBinding
                RecordImageViewHolder(bindingImage)*/
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
}

class MultiViewImageAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<RecordImage, MultiViewImageAdapter.MultiViewImageViewHolder>(
        diffUtil
    ) {
    interface OnItemClickListener {
        fun onItemClick(view: View, ind: Int)
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

    class MultiViewImageViewHolder(val binding: ItemRecyclerRecordManyImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecordImage) {
            binding.imageItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewImageViewHolder {
        /*val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_many_images,
            parent,
            false
        ) as ItemRecyclerManyImagesBinding
        return MultiViewImageViewHolder(binding)*/
        return MultiViewImageViewHolder(
            ItemRecyclerRecordManyImagesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MultiViewImageViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.ivItemImage.setOnClickListener { view ->
            onItemClickListener.onItemClick(view, getItem(position).id)
        }
    }
}
