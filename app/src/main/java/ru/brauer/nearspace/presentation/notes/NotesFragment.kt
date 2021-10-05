package ru.brauer.nearspace.presentation.notes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.FragmentNotesBinding
import ru.brauer.nearspace.domain.entities.Note

class NotesFragment : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels()
    private var binding: FragmentNotesBinding? = null
    private val adapter: NotesAdapter by lazy {
        NotesAdapter(viewModel) { v ->
            registerForContextMenu(v)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            listOfNotes.adapter = adapter
            listOfNotes.addItemDecoration(
                DividerItemDecoration(
                    view.context,
                    LinearLayoutManager.VERTICAL
                )
            )

            buttonFabAdd.setOnClickListener {
                showEditDialog(null) { _, result ->
                    result.getParcelable<Note>(
                        NoteEditFragment.KEY_FRAGMENT_EDIT_RESULT
                    )?.let {
                        viewModel.notes += it
                    }
                    adapter.notifyItemChanged(viewModel.notes.size - 1)
                }
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val menuInflater: MenuInflater = requireActivity().menuInflater
        menuInflater.inflate(R.menu.context_menu_item, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.context_menu_item_delete -> {
                binding?.apply {
                    val contextMenuPosition = adapter.contextMenuPosition
                    if (contextMenuPosition != RecyclerView.NO_POSITION) {
                        viewModel.notes.removeAt(contextMenuPosition)
                        adapter.notifyItemRemoved(contextMenuPosition)
                    }
                }
                true
            }
            R.id.context_menu_item_edit -> {
                val contextMenuPosition = adapter.contextMenuPosition
                if (contextMenuPosition != RecyclerView.NO_POSITION) {
                    showEditDialog(viewModel.notes[contextMenuPosition]) { _, result ->
                        result.getParcelable<Note>(
                            NoteEditFragment.KEY_FRAGMENT_EDIT_RESULT
                        )?.let {
                            viewModel.notes[contextMenuPosition] = it
                        }
                        adapter.notifyItemChanged(contextMenuPosition)
                    }
                }
                true
            }
            else -> false
        }
    }

    private fun showEditDialog(
        note: Note?,
        callback: (requestKey: String, result: Bundle) -> Unit
    ) {
        childFragmentManager.setFragmentResultListener(
            NoteEditFragment.KEY_FRAGMENT_EDIT_RESULT,
            viewLifecycleOwner,
            callback
        )
        NoteEditFragment
            .newInstance(note)
            .show(childFragmentManager, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NotesFragment()
    }
}