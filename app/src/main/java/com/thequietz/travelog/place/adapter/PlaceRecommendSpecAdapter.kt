package com.thequietz.travelog.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceRecommendSpecBinding
import com.thequietz.travelog.place.model.PlaceRecommendModel
import com.thequietz.travelog.place.view.PlaceRecommendFragmentDirections

class PlaceRecommendSpecAdapter(private val parentFragment: Fragment) :
    ListAdapter<PlaceRecommendModel, PlaceRecommendSpecAdapter.ViewHolder>(
        PlaceRecommendSpecDiffUtil()
    ) {
    private val gson = Gson()

    class ViewHolder(
        private val binding: ItemRecyclerPlaceRecommendSpecBinding,
        private val gson: Gson
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PlaceRecommendModel, fragment: Fragment) {
            Glide.with(binding.ivPlaceRecommendSpec)
                .load(model.imageUrl)
                .centerCrop()
                .override(
                    binding.ivPlaceRecommendSpec.measuredWidth,
                    binding.ivPlaceRecommendSpec.measuredHeight
                )
                .into(binding.ivPlaceRecommendSpec)

            binding.model = model
            binding.root.setOnClickListener {
                val param = gson.toJson(model)
                val action =
                    PlaceRecommendFragmentDirections.actionPlaceRecommendFragmentToPlaceDetailFragment(
                        param, true, isGuide = false
                    )
                fragment.findNavController().navigate(action)
            }
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
        val viewHolder = ViewHolder(binding, gson)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), parentFragment)
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
