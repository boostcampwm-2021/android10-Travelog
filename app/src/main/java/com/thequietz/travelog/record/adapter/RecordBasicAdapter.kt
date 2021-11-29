package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicBinding
import com.thequietz.travelog.databinding.ItemRecyclerRecordBasicHeaderBinding
import com.thequietz.travelog.record.model.RecordBasicItem

sealed class RecordBasicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class RecordBasicHeaderViewHolder(
        private val binding: ItemRecyclerRecordBasicHeaderBinding,
        private val navigateToRecordAddUi: (String) -> Unit,
        private val updateTargetList: (String) -> Unit
    ) : RecordBasicViewHolder(binding.root) {
        override fun <T : RecordBasicItem> bind(item: T) = with(binding) {
            item as RecordBasicItem.RecordBasicHeader
            root.setOnClickListener {
                updateTargetList(item.day)
            }
            btnItemRecordBasicHeaderAddRecord.setOnClickListener {
                navigateToRecordAddUi.invoke(item.day)
            }
            tvItemRecordBasicHeaderTitle.text = item.day
            tvItemRecordBasicHeaderDate.text = item.date
        }
    }

    class RecordBasicItemViewHolder(
        private val binding: ItemRecyclerRecordBasicBinding,
        private val navigateToRecordViewUi: (String, String) -> Unit,
        private val showMenu: (View, Int) -> Unit
    ) : RecordBasicViewHolder(binding.root) {
        override fun <T : RecordBasicItem> bind(item: T) = with(binding) {
            item as RecordBasicItem.TravelDestination
            root.setOnClickListener {
                navigateToRecordViewUi.invoke(item.day, item.name)
            }
            btnItemRecordBasicMore.setOnClickListener {
                showMenu.invoke(it, absoluteAdapterPosition)
            }
            tvItemRecordBasicTitle.text = item.name
            /*
            val adapter =
                RecordPhotoAdapter(
                    navigateToRecordViewUi = navigateToRecordViewUi,
                    recordBasicItem = item
                )
            rvItemRecordBasic.adapter = adapter
            if (item.images.first() == "") return
            adapter.submitList(item.images)
             */
        }
    }

    abstract fun <T : RecordBasicItem> bind(item: T)
}

class RecordBasicAdapter(
    private val navigateToRecordViewUi: (String, String) -> Unit,
    private val navigateToRecordAddUi: (String) -> Unit,
    private val showMenu: (View, Int) -> Unit,
    private val updateTargetList: (String) -> Unit
) : ListAdapter<RecordBasicItem, RecordBasicViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is RecordBasicItem.RecordBasicHeader -> HEADER_TYPE
            else -> ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordBasicViewHolder {
        return when (viewType) {
            HEADER_TYPE -> {
                RecordBasicViewHolder.RecordBasicHeaderViewHolder(
                    ItemRecyclerRecordBasicHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    navigateToRecordAddUi,
                    updateTargetList
                )
            }
            else -> {
                RecordBasicViewHolder.RecordBasicItemViewHolder(
                    ItemRecyclerRecordBasicBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    navigateToRecordViewUi,
                    showMenu
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecordBasicViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private const val HEADER_TYPE = 0
        private const val ITEM_TYPE = 1

        private val diffUtil = object : DiffUtil.ItemCallback<RecordBasicItem>() {
            override fun areItemsTheSame(
                oldItem: RecordBasicItem,
                newItem: RecordBasicItem
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(
                oldItem: RecordBasicItem,
                newItem: RecordBasicItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
