package ru.brauer.nearspace.domain.repository

import ru.brauer.nearspace.domain.entities.Note

interface NotesRepository {
    val notes: List<Note>
}