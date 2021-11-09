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

class MultiViewAdapter(val innerViewModel: InnerViewModel) : ListAdapter<MyRecord, RecyclerView.ViewHolder>(
    diffUtil
) {
    private val viewPool = RecyclerView.RecycledViewPool()
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
                RecordImageViewHolder(bindingImage, innerViewModel)
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
                val temp = getItem(position) as MyRecord.RecordImageList
                innerViewModel.setCurrentData(temp.list)
                (holder as RecordImageViewHolder).bind()
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
        val binding: ItemRecyclerRecyclerBinding,
        val innerViewModel: InnerViewModel,
    ) : RecyclerView.ViewHolder(binding.root) {
        var adapter = MultiViewImageAdapter2(mutableListOf<RecordImage>())
        init {
            binding.rvItemManyImage.adapter = adapter
        }
        fun bind() {
            innerViewModel.currentList.value?.let {
                adapter.data = it.toMutableList()
            }
            adapter.notifyDataSetChanged()
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

class MultiViewImageAdapter2(var data: MutableList<RecordImage>) : RecyclerView.Adapter<MultiViewImageAdapter2.MultiViewImageAdapter2ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewImageAdapter2ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_many_images,
            parent,
            false
        ) as ItemRecyclerManyImagesBinding
        return MultiViewImageAdapter2ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MultiViewImageAdapter2ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }
    class MultiViewImageAdapter2ViewHolder(val binding: ItemRecyclerManyImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: RecordImage) {
            println(image.toString())
            binding.imageItem = image
        }
    }
}
