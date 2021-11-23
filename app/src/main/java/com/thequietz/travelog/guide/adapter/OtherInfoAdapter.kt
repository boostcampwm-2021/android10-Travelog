package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thequietz.travelog.databinding.ItemRecyclerOtherInfoBinding
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.guide.view.OtherInfoFragmentDirections
import com.thequietz.travelog.place.model.PlaceGeometry
import com.thequietz.travelog.place.model.PlaceLocation
import com.thequietz.travelog.place.model.PlaceSearchModel

class OtherInfoAdapter : androidx.recyclerview.widget.ListAdapter<RecommendPlace, OtherInfoAdapter.OtherInfoViewHolder>(
    RecommendPlaceDiffUtilCallback()
) {
    class OtherInfoViewHolder(val binding: ItemRecyclerOtherInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendPlace) {
            binding.item = item
            binding.executePendingBindings()
            itemView.setOnClickListener {
                val param = Gson().toJson(
                    PlaceSearchModel(
                        item.name,
                        "23",
                        PlaceGeometry(
                            PlaceLocation(
                                item.latitude, item.longitude
                            )
                        )
                    )
                )
                val action = OtherInfoFragmentDirections
                    .actionOtherInfoFragmentToPlaceDetailFragmentFromGuide(
                        param, true
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
