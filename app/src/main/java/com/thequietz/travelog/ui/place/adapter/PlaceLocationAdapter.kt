package com.thequietz.travelog.ui.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceLocationBinding
import com.thequietz.travelog.ui.schedule.model.SchedulePlaceModel

class PlaceLocationAdapter(
    private val listener: OnItemClickListener
) :
    ListAdapter<SchedulePlaceModel, PlaceLocationAdapter.ViewHolder>(PlaceLocationDiffUtil()) {

    class ViewHolder(
        val binding: ItemRecyclerPlaceLocationBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: SchedulePlaceModel, listener: OnItemClickListener) {
            binding.model = model
            binding.btnLocation.setOnClickListener {
                listener.onItemClick(model)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerPlaceLocationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_place_location,
            parent,
            false
        )
        val itemViewHolder = ViewHolder(binding)
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface OnItemClickListener {
        fun onItemClick(model: SchedulePlaceModel)
    }
}

private class PlaceLocationDiffUtil() : DiffUtil.ItemCallback<SchedulePlaceModel>() {
    override fun areItemsTheSame(
        oldItem: SchedulePlaceModel,
        newItem: SchedulePlaceModel
    ): Boolean {
        return oldItem.cityName == newItem.cityName
    }

    override fun areContentsTheSame(
        oldItem: SchedulePlaceModel,
        newItem: SchedulePlaceModel
    ): Boolean {
        return oldItem == newItem
    }
}
