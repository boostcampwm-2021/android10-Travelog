package com.thequietz.travelog.confirm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerConfirmPageBinding
import com.thequietz.travelog.schedule.model.ScheduleDetailModel

class ConfirmPagerAdapter :
    ListAdapter<ScheduleDetailModel, ConfirmPagerAdapter.ViewHolder>(ConfirmPagerDiffUtil()) {

    class ViewHolder(val binding: ItemRecyclerConfirmPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ScheduleDetailModel?) {
            binding.model = model

            val apiKey = BuildConfig.GOOGLE_MAP_KEY
            val reservedSrc = "Aap_uECPoBEl_RLC8YDMxcIulQ7fGfCdt2Jvn677nHTQPa0VWcAnYCeuogtdj7-V--YYtaOFZf1W1UGA__J62jjaf32djXMLYkbkYuR4KZuiP20t-jKmK56akJZr9TQ9hcs_RO1_iWjo6FrcloCSgJ_e5Gd20nC0AfZiu3AYuP4sVgG6j-kj"

            val imageUrl = model?.destination?.images?.firstOrNull()
            val imageId = model?.destination?.images?.firstOrNull()?.imageId
            val imageSrc = imageUrl?.imageUrl
                ?: "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${imageId ?: reservedSrc}&key=$apiKey"

            Glide.with(binding.ivConfirmPage)
                .load(imageSrc)
                .centerCrop()
                .into(binding.ivConfirmPage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerConfirmPageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_confirm_page,
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class ConfirmPagerDiffUtil : DiffUtil.ItemCallback<ScheduleDetailModel>() {
    override fun areItemsTheSame(
        oldItem: ScheduleDetailModel,
        newItem: ScheduleDetailModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ScheduleDetailModel,
        newItem: ScheduleDetailModel
    ): Boolean {
        return oldItem == newItem
    }
}
