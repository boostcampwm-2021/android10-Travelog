package com.thequietz.travelog.schedule.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleDetailAddBinding
import com.thequietz.travelog.databinding.ItemRecyclerScheduleDetailContentBinding
import com.thequietz.travelog.databinding.ItemRecyclerScheduleDetailHeaderBinding
import com.thequietz.travelog.schedule.data.ScheduleDetailItem
import com.thequietz.travelog.schedule.data.TYPE_CONTENT
import com.thequietz.travelog.schedule.data.TYPE_HEADER
import com.thequietz.travelog.schedule.viewmodel.ScheduleDetailViewModel

class ScheduleDetailAdapter(val viewModel: ScheduleDetailViewModel, val onAdd: () -> (Unit)) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ScheduleTouchHelperCallback.OnItemMoveListener {
    var item = listOf<ScheduleDetailItem>()

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (toPosition != 0) {
            viewModel.itemMove(fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
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
        when (holder.itemViewType) {
            TYPE_HEADER -> {
                (holder as HeaderViewHolder).bind(item[position])
            }
            TYPE_CONTENT -> {
                (holder as ContentViewHolder).bind(item[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemViewType(position: Int): Int {
        return item[position].type
    }

    inner class HeaderViewHolder(val binding: ItemRecyclerScheduleDetailHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleDetailItem) {
            binding.item = item
            binding.viewModel = viewModel
            binding.setOnAddClickListener {
                if (item.index != null && item.name != null) {
                    onAdd()
                }
                (bindingAdapter as ScheduleDetailAdapter).onAdd
            }
        }
    }

    inner class ContentViewHolder(val binding: ItemRecyclerScheduleDetailContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleDetailItem) {
            binding.item = item
            binding.viewModel = viewModel
            item.color?.let {
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
