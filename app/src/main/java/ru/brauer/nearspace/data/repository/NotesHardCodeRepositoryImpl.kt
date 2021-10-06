package ru.brauer.nearspace.data.repository

import ru.brauer.nearspace.domain.entities.Note
import ru.brauer.nearspace.domain.repository.NotesRepository

class NotesHardCodeRepositoryImpl : NotesRepository {
    override val notes: List<Note>
        get() = listOf(
            Note(
                "watch the stars",
                "Astronomy can be daunting for beginners — after all there’s a whole " +
                        "universe out there! But stargazing basics don’t have to be hard. Sky & " +
                        "Telescope editors (with more than 100 years of collective experience) are " +
                        "here to help you learn your way around the night sky. Whether you’re " +
                        "looking for your first telescope, trying to learn the constellations, " +
                        "or want to learn to use star charts, you’ve come to the right place. " +
                        "The best way to start exploring the night sky is with the unaided eye. " +
                        "Our star wheels are easy to use, and in no time you’ll learn the " +
                        "constellations and names of the stars. And that’s only the beginning!"
            ),
            Note("explore constellations", ""),
            Note("сheck satellite imagery", ""),
            Note("buy a telescope", "")
        )
}