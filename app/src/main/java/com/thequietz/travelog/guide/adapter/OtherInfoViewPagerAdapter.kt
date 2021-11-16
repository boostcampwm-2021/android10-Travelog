package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemViewpagerOtherInfoBinding
import com.thequietz.travelog.guide.Place

class OtherInfoViewPagerAdapter : ListAdapter<Place, OtherInfoViewPagerAdapter.OtherInfoViewPagerViewHolder>(
    PlaceDiffUtilCallback()
) {
    class OtherInfoViewPagerViewHolder(val binding: ItemViewpagerOtherInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OtherInfoViewPagerViewHolder {
        return OtherInfoViewPagerViewHolder(
            ItemViewpagerOtherInfoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OtherInfoViewPagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
