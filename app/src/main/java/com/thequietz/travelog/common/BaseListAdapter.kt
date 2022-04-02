package com.thequietz.travelog.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T, VDB : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<VDB>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VDB> {
        val binding = createBinding(parent, viewType)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VDB>, position: Int) {
        bind(holder.binding, position)
        holder.binding.executePendingBindings()
    }

    abstract fun createBinding(parent: ViewGroup, viewType: Int): VDB

    abstract fun bind(binding: VDB, position: Int)
}
