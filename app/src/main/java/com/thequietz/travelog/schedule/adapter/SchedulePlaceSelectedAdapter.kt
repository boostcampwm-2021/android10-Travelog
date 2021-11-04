package com.thequietz.travelog.schedule.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerSchedulePlaceSelectedBinding
import com.thequietz.travelog.schedule.model.PlaceSelected

class SchedulePlaceSelectedAdapter(
    private val placeSelectedList: List<PlaceSelected>,
    private val clickListener: OnItemClickListener,
) : RecyclerView.Adapter<SchedulePlaceSelectedAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRecyclerSchedulePlaceSelectedBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var binding: ItemRecyclerSchedulePlaceSelectedBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_schedule_place_selected,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.let {
            it.btnSelectedItem.text = placeSelectedList[position].value
            it.btnSelectedItem.setOnClickListener { v ->
                clickListener.onItemClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return placeSelectedList.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}
