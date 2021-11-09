package com.thequietz.travelog.place.adapter

import androidx.recyclerview.widget.DiffUtil
import com.thequietz.travelog.place.model.PlaceSearchModel

object PlaceSearchDiffUtil : DiffUtil.ItemCallback<PlaceSearchModel>() {
    override fun areItemsTheSame(oldItem: PlaceSearchModel, newItem: PlaceSearchModel): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: PlaceSearchModel, newItem: PlaceSearchModel): Boolean {
        return oldItem == newItem
    }
}
