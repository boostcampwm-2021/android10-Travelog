package com.thequietz.travelog.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerPlaceRecommendBinding
import com.thequietz.travelog.place.model.PlaceRecommendModel
import com.thequietz.travelog.place.model.PlaceRecommendWithList

class PlaceRecommendAdapter(private val parentFragment: Fragment) :
    ListAdapter<PlaceRecommendWithList, PlaceRecommendAdapter.ViewHolder>(PlaceRecommendDiffUtil()) {
    class ViewHolder(private val binding: ItemRecyclerPlaceRecommendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String, data: List<PlaceRecommendModel>?, fragment: Fragment) {
            binding.tvPlaceRecommend.text = title
            binding.rvPlaceRecommend.adapter = PlaceRecommendSpecAdapter(fragment)

            (binding.rvPlaceRecommend.adapter as PlaceRecommendSpecAdapter).submitList(data)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerPlaceRecommendBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_place_recommend,
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.title, item.list.value, parentFragment)
    }
}

private class PlaceRecommendDiffUtil : DiffUtil.ItemCallback<PlaceRecommendWithList>() {
    override fun areItemsTheSame(
        oldItem: PlaceRecommendWithList,
        newItem: PlaceRecommendWithList
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: PlaceRecommendWithList,
        newItem: PlaceRecommendWithList
    ): Boolean {
        return oldItem == newItem
    }
}
