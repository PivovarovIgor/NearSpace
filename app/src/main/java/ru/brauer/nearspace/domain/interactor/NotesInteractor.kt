package ru.brauer.nearspace.domain.interactor

import ru.brauer.nearspace.domain.entities.Note

interface NotesInteractor {
    val notes: List<Note>
}
