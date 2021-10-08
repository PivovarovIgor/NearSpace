package ru.brauer.nearspace.presentation.notes

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {

    companion object {
        private const val TAG = "ItemTouchHelperCallback"
    }

    override fun isLongPressDragEnabled(): Boolean {
        Log.i(TAG, "isLongPressDragEnabled")
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        Log.i(TAG, "isItemViewSwipeEnabled")
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        Log.i(TAG, "getMovementFlags")
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Log.i(TAG, "onMove")
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.i(TAG, "onSwiped")
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        Log.i(
            TAG, "onSelectedChanged actionState=" + when (actionState) {
                ItemTouchHelper.ACTION_STATE_IDLE -> "IDLE"
                ItemTouchHelper.ACTION_STATE_DRAG -> "DRAG"
                ItemTouchHelper.ACTION_STATE_SWIPE -> "SWIPE"
                else -> actionState.toString()
            }
        )
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        Log.i(TAG, "clearView")
        super.clearView(recyclerView, viewHolder)
        val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
        itemViewHolder.onItemClear()
    }
}
