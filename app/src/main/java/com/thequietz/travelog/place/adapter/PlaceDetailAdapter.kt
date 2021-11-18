package com.thequietz.travelog.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceDetailBinding
import com.thequietz.travelog.place.model.PlaceDetailImage

class PlaceDetailAdapter(private val isRecommended: Boolean) :
    ListAdapter<PlaceDetailImage, PlaceDetailAdapter.ViewHolder>(
        PlaceDetailDiffUtil()
    ) {

    class ViewHolder(private val binding: ItemRecyclerPlaceDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PlaceDetailImage, isRecommended: Boolean) {
            val imageUrl = if (isRecommended) {
                model.imageUrl
            } else {
                val apiKey = BuildConfig.GOOGLE_MAP_KEY
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${model.imageId}&key=$apiKey"
            }
            binding.imageId = model.imageId
            Glide.with(binding.root)
                .load(imageUrl)
                .centerCrop()
                .override(
                    binding.ivItemPlaceDetailPhoto.measuredWidth,
                    binding.ivItemPlaceDetailPhoto.measuredHeight
                )
                .into(binding.ivItemPlaceDetailPhoto)
            binding.executePendingBindings()
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
        holder.bind(getItem(position), isRecommended)
    }
}

private class PlaceDetailDiffUtil : DiffUtil.ItemCallback<PlaceDetailImage>() {
    override fun areItemsTheSame(
        oldItem: PlaceDetailImage,
        newItem: PlaceDetailImage
    ) = oldItem.imageId == newItem.imageId

    override fun areContentsTheSame(
        oldItem: PlaceDetailImage,
        newItem: PlaceDetailImage
    ) = oldItem == newItem
}
