package com.thequietz.travelog.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceRecommendSpecBinding
import com.thequietz.travelog.place.model.PlaceRecommendModel

class PlaceRecommendSpecAdapter :
    ListAdapter<PlaceRecommendModel, PlaceRecommendSpecAdapter.ViewHolder>(
        PlaceRecommendSpecDiffUtil()
    ) {
    class ViewHolder(private val binding: ItemRecyclerPlaceRecommendSpecBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PlaceRecommendModel) {
            Glide.with(binding.ivPlaceRecommendSpec)
                .load(model.imageUrl)
                .centerCrop()
                .override(
                    binding.ivPlaceRecommendSpec.measuredWidth,
                    binding.ivPlaceRecommendSpec.measuredHeight
                )
                .into(binding.ivPlaceRecommendSpec)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerPlaceRecommendSpecBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_place_recommend_spec,
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class PlaceRecommendSpecDiffUtil : DiffUtil.ItemCallback<PlaceRecommendModel>() {
    override fun areItemsTheSame(
        oldItem: PlaceRecommendModel,
        newItem: PlaceRecommendModel
    ): Boolean {
        return oldItem.contentId == newItem.contentId
    }

    override fun areContentsTheSame(
        oldItem: PlaceRecommendModel,
        newItem: PlaceRecommendModel
    ): Boolean {
        return oldItem == newItem
    }
}
