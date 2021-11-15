package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerGuidePlaceAllBinding
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.view.SpecificGuideFragmentDirections

class AllPlaceAdapter() : androidx.recyclerview.widget.ListAdapter<Place, AllPlaceAdapter.AllPlaceViewHolder>(
    diffUtil
) {
    class AllPlaceViewHolder(val binding: ItemRecyclerGuidePlaceAllBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place?) {
            binding.item = item
            binding.executePendingBindings()
            val temp = mutableListOf<Place>()
            item?.let {
                temp.add(
                    it.copy(
                        areaCode = "31",
                        url = "http://tong.visitkorea.or.kr/cms/resource/36/2364836_image2_1.jpg",
                        name = "고양시"
                    )
                )
                temp.add(
                    it.copy(
                        areaCode = "6",
                        url = "http://tong.visitkorea.or.kr/cms/resource/43/2757743_image2_1.jpg",
                        name = "부산"
                    )
                )
                temp.add(
                    it.copy(
                        areaCode = "7",
                        url = "http://tong.visitkorea.or.kr/cms/resource/67/2558467_image2_1.jpg",
                        name = "울산"
                    )
                )
            }

            itemView.setOnClickListener {
                val action = SpecificGuideFragmentDirections
                    // .actionSpecificGuideFragmentToOtherInfoFragment(arrayOf(item!!))
                    .actionSpecificGuideFragmentToOtherInfoFragment(temp.toTypedArray())
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPlaceViewHolder {
        return AllPlaceViewHolder(
            ItemRecyclerGuidePlaceAllBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AllPlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem == newItem
            }
        }
    }
}
