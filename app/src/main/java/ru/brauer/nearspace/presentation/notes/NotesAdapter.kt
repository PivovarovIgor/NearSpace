package ru.brauer.nearspace.presentation.notes

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.nearspace.databinding.RecyclerViewItemOfNotesBinding
import ru.brauer.nearspace.domain.entities.Note

class NotesAdapter(
    private val viewModel: NotesViewModel,
    private val registerForContextMenu: (view: View) -> Unit
) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    var startDrag: (vh: NotesAdapter.ViewHolder) -> Unit = {}

    companion object {
        private const val MOVING_UP = -1
        private const val MOVING_DOWN = 1
        private const val DURATION_ANIMATION_ITEM = 5000L
    }

    var contextMenuPosition: Int = RecyclerView.NO_POSITION
        private set

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewItemOfNotesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        registerForContextMenu(binding.noteText)

        val vh = ViewHolder(binding)
        binding.run {

            buttonNoteUp.setOnClickListener {
                moveItem(MOVING_UP) { vh.adapterPosition }
            }

            buttonNoteDown.setOnClickListener {
                moveItem(MOVING_DOWN) { vh.adapterPosition }
            }

            noteText.setOnLongClickListener {
                contextMenuPosition = vh.adapterPosition
                it.showContextMenu()
                true
            }
            noteText.setOnClickListener {
                viewModel.notes[vh.adapterPosition] =
                    (viewModel.notes[vh.adapterPosition].first to
                            !viewModel.notes[vh.adapterPosition].second)
                notifyItemChanged(vh.adapterPosition)
            }

            dragHandler.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    startDrag(vh)
                }
                false
            }
        }

        return vh
    }

    private inline fun moveItem(offset: Int, position: () -> Int) {
        val pos = position()
        require(offset == MOVING_UP || offset == MOVING_DOWN)
        if (pos != RecyclerView.NO_POSITION) {
            if (offset == -1 && pos > 0
                || offset == 1 && pos < viewModel.notes.size - 1
            ) {
                swapPairItems(pos, pos + offset)
            }
        }
    }

    private fun swapPairItems(
        fromPosition: Int,
        toPosition: Int
    ) {
        viewModel.run {
            val noteHolder = notes[fromPosition]
            notes[fromPosition] = notes[toPosition]
            notes[toPosition] = noteHolder
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.notes[position])
    }

    override fun getItemCount(): Int = viewModel.notes.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) =
        swapPairItems(fromPosition, toPosition)

    override fun onItemDismiss(position: Int) {
        viewModel.notes.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(private val binding: RecyclerViewItemOfNotesBinding) :
        RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

        private var animator: Animator? = null

        fun bind(noteRec: Pair<Note, Boolean>) {
            binding.run {
                animator?.let {
                    if (it.isStarted) {
                        it.cancel()
                    }
                    animator = null
                }
                itemView.setBackgroundColor(Color.WHITE)
                noteText.text = noteRec.first.noteText
                noteDescription.text = noteRec.first.description
                noteDescription.visibility = if (noteRec.second) View.VISIBLE else View.GONE
            }
        }

        override fun onItemSelected() {
            animate(Color.WHITE, Color.TRANSPARENT)
        }

        override fun onItemClear() {
            animate(Color.TRANSPARENT, Color.WHITE)
        }

        private fun animate(beginValue: Int, endValue: Int) {
            animator?.let {
                if (it.isStarted) {
                    it.pause()
                }
            }
            val startValue = itemView.background.let {
                (it as? ColorDrawable)?.color ?: beginValue
            }
            animator = ValueAnimator.ofObject(ArgbEvaluator(), startValue, endValue).apply {
                duration = DURATION_ANIMATION_ITEM
                addUpdateListener {
                    itemView.setBackgroundColor(it.animatedValue as Int)
                }
            }.also { it.start() }
        }
    }
}