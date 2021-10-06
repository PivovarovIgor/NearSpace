package ru.brauer.nearspace.presentation.notes

import androidx.lifecycle.ViewModel
import ru.brauer.nearspace.data.repository.NotesHardCodeRepositoryImpl
import ru.brauer.nearspace.domain.entities.Note
import ru.brauer.nearspace.domain.interactor.NotesInteractor
import ru.brauer.nearspace.domain.interactor.NotesInteractorImpl

class NotesViewModel(
    interactor: NotesInteractor = NotesInteractorImpl(NotesHardCodeRepositoryImpl())
) : ViewModel() {

    val notes: MutableList<Pair<Note, Boolean>> = mutableListOf()

    init {
        interactor.notes.forEach { notes += it to false }
    }
}