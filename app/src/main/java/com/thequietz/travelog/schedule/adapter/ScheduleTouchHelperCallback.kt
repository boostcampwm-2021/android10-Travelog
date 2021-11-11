package com.thequietz.travelog.schedule.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ScheduleTouchHelperCallback(private val itemMoveListener: OnItemMoveListener) :
    ItemTouchHelper.Callback() {

    private var isMoved = false

    interface OnItemMoveListener {
        fun onItemMove(fromPosition: Int, toPosition: Int)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        isMoved = false
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        itemMoveListener.onItemMove(
            viewHolder.bindingAdapterPosition,
            target.bindingAdapterPosition
        )
        isMoved = true
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}
