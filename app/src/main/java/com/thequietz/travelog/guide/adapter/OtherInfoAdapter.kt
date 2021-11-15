package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerOtherInfoBinding
import com.thequietz.travelog.guide.RecommendPlace

class OtherInfoAdapter : androidx.recyclerview.widget.ListAdapter<RecommendPlace, OtherInfoAdapter.OtherInfoViewHolder>(
    RecommendPlaceDiffUtilCallback()
) {
    class OtherInfoViewHolder(val binding: ItemRecyclerOtherInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendPlace) {
            binding.item = item
            binding.executePendingBindings()
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "${item.name}", Toast.LENGTH_SHORT).show()
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
