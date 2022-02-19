package com.thequietz.travelog.ui.schedule.adapter

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.ui.schedule.data.TYPE_CONTENT

class ScheduleTouchHelperCallback(private val itemMoveListener: OnItemMoveListener) :
    ItemTouchHelper.Callback() {

    private var isMoved = false
    private var positionFrom = -1
    private var positionTo = -1

    interface OnItemMoveListener {
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onItemDropped(fromPosition: Int, toPosition: Int)
        fun onItemSwiped(position: Int)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                if (viewHolder != null) {
                    positionFrom = viewHolder.bindingAdapterPosition
                    positionTo = positionFrom
                    Log.d("ACTION_STATE_DRAG", "$positionFrom / $positionTo")
                }
            }
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                if (positionFrom != -1 && positionTo != -1 && positionFrom != positionTo && isMoved) {
                    Log.d("ACTION_STATE_IDLE", "$positionFrom / $positionTo")
                    itemMoveListener.onItemDropped(positionFrom, positionTo)

                    isMoved = false
                    positionTo = -1
                    positionFrom = -1
                }
            }
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && isMoved) {
            Log.d("ACTION_STATE_IDLE", "$positionFrom / $positionTo")
            itemMoveListener.onItemDropped(positionFrom, positionTo)

            isMoved = false
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        if (viewHolder.itemViewType != TYPE_CONTENT)
            return makeMovementFlags(0, 0)

        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        val fromPos = viewHolder.bindingAdapterPosition
        val toPos = target.bindingAdapterPosition
        itemMoveListener.onItemMove(fromPos, toPos)

        positionTo = if (fromPos > toPos) {
            viewHolder.bindingAdapterPosition - 1
        } else if (fromPos < toPos) {
            viewHolder.bindingAdapterPosition + 1
        } else {
            viewHolder.bindingAdapterPosition
        }

        isMoved = true
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemMoveListener.onItemSwiped(viewHolder.bindingAdapterPosition)
    }
}
