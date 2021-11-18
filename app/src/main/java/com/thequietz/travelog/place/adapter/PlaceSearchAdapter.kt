package com.thequietz.travelog.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceSearchBinding
import com.thequietz.travelog.place.model.PlaceSearchModel

class PlaceSearchAdapter(
    private val listener: OnItemClickListener
) :
    ListAdapter<PlaceSearchModel, PlaceSearchAdapter.ViewHolder>(PlaceSearchDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerPlaceSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_place_search,
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(private val binding: ItemRecyclerPlaceSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PlaceSearchModel, position: Int) {
            binding.model = model
            binding.tvPlaceSearchItem.setOnClickListener {
                listener.onClickItem(model, position)
            }

            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onClickItem(model: PlaceSearchModel, position: Int): Boolean
    }
}

private class PlaceSearchDiffUtil : DiffUtil.ItemCallback<PlaceSearchModel>() {
    override fun areItemsTheSame(oldItem: PlaceSearchModel, newItem: PlaceSearchModel) =
        oldItem.placeId == newItem.placeId

    override fun areContentsTheSame(oldItem: PlaceSearchModel, newItem: PlaceSearchModel) =
        oldItem == newItem
}
