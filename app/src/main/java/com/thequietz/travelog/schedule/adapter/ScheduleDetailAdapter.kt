package com.thequietz.travelog.schedule.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleDetailAddBinding
import com.thequietz.travelog.databinding.ItemRecyclerScheduleDetailContentBinding
import com.thequietz.travelog.databinding.ItemRecyclerScheduleDetailHeaderBinding
import com.thequietz.travelog.schedule.data.ScheduleDetailItem
import com.thequietz.travelog.schedule.data.TYPE_CONTENT
import com.thequietz.travelog.schedule.data.TYPE_HEADER

class ScheduleDetailAdapter(
    var onAdd: () -> (Unit),
    var onDelete: (Int) -> (Unit),
    var onMoveCompleted: (MutableList<ScheduleDetailItem>) -> (Unit),
    var changeSelected: (Int, String) -> (Unit)
) :
    ListAdapter<ScheduleDetailItem, RecyclerView.ViewHolder>(ScheduleDiffCallback()),
    ScheduleTouchHelperCallback.OnItemMoveListener {

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (toPosition != 0) {
            val newList = currentList.toMutableList()
            if (fromPosition >= toPosition) {
                val data = newList.removeAt(fromPosition)
                newList.add(toPosition, data)
            } else {
                val data = newList.removeAt(fromPosition)
                if (toPosition + 1 >= newList.size)
                    newList.add(toPosition, data)
                else
                    newList.add(toPosition + 1, data)
            }
            submitList(newList)
        }
    }

    override fun onItemDropped(fromPosition: Int, toPosition: Int) {
        if (toPosition != 0) {
            onMoveCompleted(currentList.toMutableList())
        }
    }

    override fun onItemSwiped(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
        onDelete(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding =
                    ItemRecyclerScheduleDetailHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                HeaderViewHolder(binding)
            }
            TYPE_CONTENT -> {
                val binding =
                    ItemRecyclerScheduleDetailContentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ContentViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemRecyclerScheduleDetailAddBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                AddViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (holder.itemViewType) {
            TYPE_HEADER -> {
                (holder as HeaderViewHolder).bind(getItem(position))
            }
            else -> {
                (holder as ContentViewHolder).bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    inner class HeaderViewHolder(val binding: ItemRecyclerScheduleDetailHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleDetailItem) {
            binding.item = item
            binding.setOnAddClickListener {
                if (item.index != null) {
                    onAdd()
                    changeSelected(item.index - 1, item.name)
                }
            }
        }
    }

    inner class ContentViewHolder(val binding: ItemRecyclerScheduleDetailContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleDetailItem) {
            binding.item = item
            item.color.let {
                binding.viewCircle.backgroundTintList =
                    ColorStateList.valueOf(Color.rgb(it.r, it.g, it.b))
            }
        }
    }

    inner class AddViewHolder(val binding: ItemRecyclerScheduleDetailAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleDetailItem) {
            binding.item = item
        }
    }
}

private class ScheduleDiffCallback : DiffUtil.ItemCallback<ScheduleDetailItem>() {
    override fun areItemsTheSame(
        oldItem: ScheduleDetailItem,
        newItem: ScheduleDetailItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ScheduleDetailItem,
        newItem: ScheduleDetailItem
    ): Boolean = oldItem == newItem
}
