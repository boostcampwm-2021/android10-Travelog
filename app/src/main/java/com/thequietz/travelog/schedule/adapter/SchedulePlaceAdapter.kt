package com.thequietz.travelog.schedule.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerSchedulePlaceBinding
import com.thequietz.travelog.schedule.model.PlaceModel

class SchedulePlaceAdapter(
    private val placeList: List<PlaceModel>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<SchedulePlaceAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRecyclerSchedulePlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var binding: ItemRecyclerSchedulePlaceBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_schedule_place,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.let {
            it.tvGuideItem.text = placeList[position].cityName
            it.ibGuideItem.setOnClickListener { v ->
                listener.onItemClick(v, position)
            }
            Glide.with(it.ibGuideItem)
                .load(placeList[position].thumbnail)
                .centerCrop()
                .override(it.ibGuideItem.measuredWidth, it.ibGuideItem.measuredHeight)
                .into(it.ibGuideItem)
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}
