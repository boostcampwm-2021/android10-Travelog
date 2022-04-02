package com.thequietz.travelog.ui.confirm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerConfirmKeyBinding

class ConfirmDayAdapter(
    private val onClickListener: OnClickListener
) :
    ListAdapter<String, ConfirmDayAdapter.ViewHolder>(ConfirmDayDiffUtil()) {

    class ViewHolder(val binding: ItemRecyclerConfirmKeyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(value: String, listener: OnClickListener, position: Int) {
            binding.value = value
            binding.btnConfirmKey.setOnClickListener {
                listener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerConfirmKeyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_confirm_key,
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener, position)
    }

    interface OnClickListener {
        fun onClick(index: Int)
    }
}

private class ConfirmDayDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
