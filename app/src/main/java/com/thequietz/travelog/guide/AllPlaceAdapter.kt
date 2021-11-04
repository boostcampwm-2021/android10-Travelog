package com.thequietz.travelog.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceAllBinding

class AllPlaceAdapter : androidx.recyclerview.widget.ListAdapter<Place, AllPlaceAdapter.AllPlaceViewHolder>(
    diffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPlaceViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_place_all,
            parent,
            false
        ) as ItemRecyclerPlaceAllBinding
        return AllPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllPlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AllPlaceViewHolder(val binding: ItemRecyclerPlaceAllBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place?) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem == newItem
            }
        }
    }
}
