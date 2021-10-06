package ru.brauer.nearspace.presentation.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.nearspace.databinding.RecyclerViewItemOfNotesBinding
import ru.brauer.nearspace.domain.entities.Note

class NotesAdapter(
    private val viewModel: NotesViewModel,
    private val registerForContextMenu: (view: View) -> Unit
) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    companion object {
        private const val MOVING_UP = -1
        private const val MOVING_DOWN = 1
    }

    var contextMenuPosition: Int = RecyclerView.NO_POSITION
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewItemOfNotesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        registerForContextMenu(binding.root)

        val vh = ViewHolder(binding)
        binding.run {
            root.setOnLongClickListener {
                contextMenuPosition = vh.adapterPosition
                it.showContextMenu()
                true
            }

            buttonNoteUp.setOnClickListener {
                moveItem(MOVING_UP) { vh.adapterPosition }
            }

            buttonNoteDown.setOnClickListener {
                moveItem(MOVING_DOWN) { vh.adapterPosition }
            }

            noteText.setOnClickListener {
                viewModel.notes[vh.adapterPosition] =
                    (viewModel.notes[vh.adapterPosition].first to
                            !viewModel.notes[vh.adapterPosition].second)
                notifyItemChanged(vh.adapterPosition)
            }
        }

        return vh
    }

    private inline fun moveItem(offset: Int, position: () -> Int) {
        val pos = position()
        require(offset == MOVING_UP || offset == MOVING_DOWN)
        if (pos != RecyclerView.NO_POSITION) {
            viewModel.run {
                if (offset == -1 && pos > 0
                    || offset == 1 && pos < notes.size - 1
                ) {
                    val noteHolder = notes[pos]
                    notes[pos] = notes[pos + offset]
                    notes[pos + offset] = noteHolder
                    notifyItemMoved(pos, pos + offset)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.notes[position])
    }

    override fun getItemCount(): Int = viewModel.notes.size

    inner class ViewHolder(private val binding: RecyclerViewItemOfNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(noteRec: Pair<Note, Boolean>) {
            binding.run {
                noteText.text = noteRec.first.noteText
                noteDescription.text = noteRec.first.description
                noteDescription.visibility = if (noteRec.second) View.VISIBLE else View.GONE
            }
        }
    }
}