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
import com.thequietz.travelog.record.viewmodel.RecordViewManyInnerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordViewManyMultiViewAdapter(
    val innerViewModel: RecordViewManyInnerViewModel
) : ListAdapter<MyRecord, RecyclerView.ViewHolder>(
    MyRecordDiffUtilCallback()
) {
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
        val binding: RecyclerRecordManyImageBinding,
        val innerViewModel: RecordViewManyInnerViewModel,
    ) : RecyclerView.ViewHolder(binding.root) {
        val adapter = MultiViewImageAdapter(
            innerViewModel,
            object : MultiViewImageAdapter.OnItemClickListener {
                override fun onItemClick(view: View, ind: Int) {
                    if (innerViewModel.deleteState.value == false) {
                        val action = RecordViewManyFragmentDirections
                            .actionRecordViewManyFragmentToRecordViewOneFragment(ind)
                        itemView.findNavController().navigate(action)
                    }
                }
            }
        )

        init {
            binding.rvItemManyImage.adapter = adapter
        }

        fun bind(imageList: MyRecord.RecordImageList) {
            adapter.submitList(imageList.list)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyRecord.RecordSchedule -> 0
            is MyRecord.RecordPlace -> 1
            is MyRecord.RecordImageList -> 2
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
            }
            ViewType.PLACE -> {
                RecordPlaceViewHolder(
                    ItemRecyclerRecordManyPlaceBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            ViewType.IMAGE -> {
                RecordImageViewHolder(
                    RecyclerRecordManyImageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    innerViewModel
                )
            }
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

class MultiViewImageAdapter(
    val innerViewModel: RecordViewManyInnerViewModel,
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<RecordImage, MultiViewImageAdapter.MultiViewImageViewHolder>(
        RecordImageDiffUtilCallback()
    ) {
    interface OnItemClickListener {
        fun onItemClick(view: View, ind: Int)
    }

    class MultiViewImageViewHolder(val binding: ItemRecyclerRecordManyImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            innerViewModel: RecordViewManyInnerViewModel,
            item: RecordImage
        ) {
            binding.imageItem = item
            binding.viewModel = innerViewModel
            innerViewModel.deleteState.observeForever {
                if (it) {
                    binding.cbDeleteCheck.visibility = View.VISIBLE
                } else {
                    binding.cbDeleteCheck.visibility = View.GONE
                    binding.cbDeleteCheck.isChecked = false
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    innerViewModel.findChecked(item.id)
                }
                withContext(Dispatchers.Main) {
                    innerViewModel.isChecked.value?.let {
                        binding.cbDeleteCheck.isChecked = it
                    }
                }
                innerViewModel.resetChecked()
            }
            binding.cbDeleteCheck.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    innerViewModel.addCheck(item.id)
                } else {
                    innerViewModel.deleteCheck(item.id)
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewImageViewHolder {
        return MultiViewImageViewHolder(
            ItemRecyclerRecordManyImagesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MultiViewImageViewHolder, position: Int) {
        holder.bind(innerViewModel, getItem(position))
        holder.binding.ivItemImage.setOnClickListener { view ->
            onItemClickListener.onItemClick(view, getItem(position).id)
            holder.binding.cbDeleteCheck.isChecked = !(holder.binding.cbDeleteCheck.isChecked)
        }
    }
}
class MyRecordDiffUtilCallback : DiffUtil.ItemCallback<MyRecord>() {
    override fun areItemsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
        return oldItem == newItem
    }
}
