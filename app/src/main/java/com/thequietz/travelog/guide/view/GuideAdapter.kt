package com.thequietz.travelog.guide.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerGuideBinding
import com.thequietz.travelog.guide.model.GuideModel

class GuideAdapter(private val areaList: List<GuideModel>) :
    RecyclerView.Adapter<GuideAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRecyclerGuideBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var binding: ItemRecyclerGuideBinding;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_guide,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvGuideItem.text = areaList[position].areaName
        Glide
            .with(binding.root)
            .load(areaList[position].thumbnail)
            .centerCrop()
            .into(binding.ibGuideItem)
    }

    override fun getItemCount(): Int {
        return areaList.size
    }
}