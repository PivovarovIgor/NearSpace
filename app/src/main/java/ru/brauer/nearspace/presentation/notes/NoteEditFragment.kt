package ru.brauer.nearspace.presentation.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import ru.brauer.nearspace.databinding.FragmentEditNoteBinding
import ru.brauer.nearspace.domain.entities.Note

class NoteEditFragment : DialogFragment() {

    companion object {

        private const val KEY_ARG_NOTE = "KEY_ARG_NOTE"
        const val KEY_FRAGMENT_EDIT_RESULT = "NoteEditFragment_EDIT_RESULT"

        fun newInstance(note: Note?) = NoteEditFragment().apply {
            arguments = Bundle().apply {
                if (note != null) {
                    putParcelable(KEY_ARG_NOTE, note)
                }
            }
        }
    }

    private var binding: FragmentEditNoteBinding? = null
    private val note: Note by lazy { arguments?.getParcelable(KEY_ARG_NOTE) ?: Note("") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            inputText.setText(note.noteText, TextView.BufferType.EDITABLE)
            apply.setOnClickListener {
                setFragmentResult(KEY_FRAGMENT_EDIT_RESULT, Bundle().apply {
                    putParcelable(KEY_FRAGMENT_EDIT_RESULT, Note(inputText.text.toString()))
                })
                dismissAllowingStateLoss()
            }
            cancel.setOnClickListener {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}