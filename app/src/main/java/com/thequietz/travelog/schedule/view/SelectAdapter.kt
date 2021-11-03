package com.thequietz.travelog.schedule.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerSelectBinding
import com.thequietz.travelog.schedule.model.SelectModel

class SelectAdapter(
    private val areaList: List<SelectModel>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<SelectAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRecyclerSelectBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var binding: ItemRecyclerSelectBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_select,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.let {
            it.tvGuideItem.text = areaList[position].cityName
            it.ibGuideItem.setOnClickListener { v ->
                listener.onItemClick(v, position)
            }
            Glide.with(it.ibGuideItem)
                .load(areaList[position].thumbnail)
                .centerCrop()
                .override(it.ibGuideItem.measuredWidth, it.ibGuideItem.measuredHeight)
                .into(it.ibGuideItem)
        }
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}
