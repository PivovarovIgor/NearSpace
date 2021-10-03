package ru.brauer.nearspace.data.repository

import ru.brauer.nearspace.domain.entities.Note
import ru.brauer.nearspace.domain.repository.NotesRepository

class NotesHardCodeRepositoryImpl : NotesRepository {
    override val notes: List<Note>
        get() = listOf(
            Note("watch the stars"),
            Note("explore constellations"),
            Note("сheck satellite imagery"),
            Note("buy a telescope")
        )
}