package com.thequietz.travelog.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceDetailBinding
import com.thequietz.travelog.place.model.PlaceDetailPhoto

class PlaceDetailAdapter : ListAdapter<PlaceDetailPhoto, PlaceDetailAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRecyclerPlaceDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PlaceDetailPhoto) {
            val apiKey = "" // BuildConfig.GOOGLE_MAP_KEY
            val imageUrl =
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${model.photoId}&key=$apiKey"
            binding.model = model
            Glide.with(binding.root)
                .load(imageUrl)
                .centerCrop()
                .into(binding.ivItemPlaceDetailPhoto)
            binding.executePendingBindings()
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PlaceDetailPhoto>() {
            override fun areItemsTheSame(
                oldItem: PlaceDetailPhoto,
                newItem: PlaceDetailPhoto
            ) = oldItem.photoId == newItem.photoId

            override fun areContentsTheSame(
                oldItem: PlaceDetailPhoto,
                newItem: PlaceDetailPhoto
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerPlaceDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_place_detail,
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
