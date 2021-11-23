package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerOtherInfoBinding
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.guide.view.OtherInfoFragmentDirections

class OtherInfoAdapter : androidx.recyclerview.widget.ListAdapter<RecommendPlace, OtherInfoAdapter.OtherInfoViewHolder>(
    RecommendPlaceDiffUtilCallback()
) {
    class OtherInfoViewHolder(val binding: ItemRecyclerOtherInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendPlace) {
            binding.item = item
            binding.executePendingBindings()
            itemView.setOnClickListener {
                val action = OtherInfoFragmentDirections
                    .actionOtherInfoFragmentToPlaceSearchFragmentFromGuide()
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
