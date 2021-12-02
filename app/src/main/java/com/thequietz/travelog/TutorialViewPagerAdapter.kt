package com.thequietz.travelog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemTutorialViewpagerImageBinding

class TutorialViewPagerAdapter : ListAdapter<TutorialImg, TutorialViewPagerAdapter.TutorialViewPagerViewHolder>(
    TutorialImgDiffUtilCallback()
) {
    class TutorialViewPagerViewHolder(val binding: ItemTutorialViewpagerImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TutorialImg) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewPagerViewHolder {
        return TutorialViewPagerViewHolder(
            ItemTutorialViewpagerImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TutorialViewPagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
data class TutorialImg(
    val url: String = ""
)
class TutorialImgDiffUtilCallback : DiffUtil.ItemCallback<TutorialImg>() {
    override fun areItemsTheSame(oldItem: TutorialImg, newItem: TutorialImg): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: TutorialImg, newItem: TutorialImg): Boolean {
        return oldItem == newItem
    }
}
