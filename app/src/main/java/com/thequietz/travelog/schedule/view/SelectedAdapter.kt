package com.thequietz.travelog.schedule.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerSelectedBinding
import com.thequietz.travelog.schedule.model.SelectedData

class SelectedAdapter(
    private val selectedDataList: List<SelectedData>,
    private val clickListener: OnItemClickListener,
) : RecyclerView.Adapter<SelectedAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRecyclerSelectedBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var binding: ItemRecyclerSelectedBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_selected,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.let {
            it.btnSelectedItem.text = selectedDataList[position].value
            it.btnSelectedItem.setOnClickListener { v ->
                clickListener.onItemClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return selectedDataList.size;
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}