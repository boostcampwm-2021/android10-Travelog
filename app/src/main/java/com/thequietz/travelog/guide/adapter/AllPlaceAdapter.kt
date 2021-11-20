package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerGuideSpecificPlaceBinding
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.view.SpecificGuideFragmentDirections

class AllPlaceAdapter() : androidx.recyclerview.widget.ListAdapter<Place, AllPlaceAdapter.AllPlaceViewHolder>(
    PlaceDiffUtilCallback()
) {
    class AllPlaceViewHolder(val binding: ItemRecyclerGuideSpecificPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            println(item.toString())
            binding.item = item
            binding.executePendingBindings()
            val temp = mutableListOf<Place>()
            temp.add(item)
            /*temp.add(
                item.copy(
                    areaCode = "31",
                    url = "http://tong.visitkorea.or.kr/cms/resource/36/2364836_image2_1.jpg",
                    name = "고양시",
                    stateName = "가평군"
                )
            )
            temp.add(
                item.copy(
                    areaCode = "6",
                    url = "http://tong.visitkorea.or.kr/cms/resource/43/2757743_image2_1.jpg",
                    name = "부산",
                    stateName = "부산시"
                )
            )
            temp.add(
                item.copy(
                    areaCode = "7",
                    url = "http://tong.visitkorea.or.kr/cms/resource/67/2558467_image2_1.jpg",
                    name = "울산",
                    stateName = "울산시"
                )
            )*/

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
