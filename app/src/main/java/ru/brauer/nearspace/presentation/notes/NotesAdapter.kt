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
        binding.root.setOnLongClickListener {
            contextMenuPosition = vh.adapterPosition
            it.showContextMenu()
            true
        }
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.notes[position])
    }

    override fun getItemCount(): Int = viewModel.notes.size

    inner class ViewHolder(private val binding: RecyclerViewItemOfNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.noteText.text = note.noteText
        }

    }
}