package ru.brauer.nearspace.domain.interactor

import ru.brauer.nearspace.domain.entities.Note
import ru.brauer.nearspace.domain.repository.NotesRepository

class NotesInteractorImpl(private val repository: NotesRepository) : NotesInteractor {
    override val notes: List<Note>
        get() = repository.notes
}