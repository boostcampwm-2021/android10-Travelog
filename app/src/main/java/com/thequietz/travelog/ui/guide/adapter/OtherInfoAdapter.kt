package com.thequietz.travelog.ui.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thequietz.travelog.databinding.ItemRecyclerOtherInfoBinding
import com.thequietz.travelog.ui.guide.RecommendPlace
import com.thequietz.travelog.ui.guide.view.OtherInfoFragmentDirections
import com.thequietz.travelog.ui.place.model.PlaceRecommendModel

class OtherInfoAdapter :
    androidx.recyclerview.widget.ListAdapter<RecommendPlace, OtherInfoAdapter.OtherInfoViewHolder>(
        OtherInfoDiffUtilCallback()
    ) {
    class OtherInfoViewHolder(val binding: ItemRecyclerOtherInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendPlace) {
            binding.item = item
            binding.executePendingBindings()
            itemView.setOnClickListener {
                val param = Gson().toJson(
                    PlaceRecommendModel(
                        item.name,
                        item.url ?: "",
                        item.description,
                        item.latitude,
                        item.longitude,
                        item.contentId,
                        item.contentTypeId
                    )
                )
                val action = OtherInfoFragmentDirections
                    .actionOtherInfoFragmentToPlaceDetailFragmentFromGuide(
                        param, isRecommended = true, isGuide = true
                    )
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherInfoViewHolder {
        return OtherInfoViewHolder(
            ItemRecyclerOtherInfoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OtherInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class OtherInfoDiffUtilCallback : DiffUtil.ItemCallback<RecommendPlace>() {
    override fun areItemsTheSame(oldItem: RecommendPlace, newItem: RecommendPlace): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RecommendPlace, newItem: RecommendPlace): Boolean {
        return oldItem == newItem
    }
}
