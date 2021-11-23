package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thequietz.travelog.databinding.ItemRecyclerGuideSpecificPlaceBinding
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.view.SpecificGuideFragmentDirections

class AllPlaceAdapter() : androidx.recyclerview.widget.ListAdapter<Place, AllPlaceAdapter.AllPlaceViewHolder>(
    PlaceDiffUtilCallback()
) {
    class AllPlaceViewHolder(val binding: ItemRecyclerGuideSpecificPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            binding.item = item
            binding.executePendingBindings()

            itemView.setOnClickListener {
                val param = Gson().toJson(item)
                val action = SpecificGuideFragmentDirections
                    .actionSpecificGuideFragmentToOtherInfoFragment(param)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPlaceViewHolder {
        return AllPlaceViewHolder(
            ItemRecyclerGuideSpecificPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AllPlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class PlaceDiffUtilCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}
