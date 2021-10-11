package ru.brauer.nearspace.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(val id: Int, val noteText: String, val description: String = "") : Parcelable {

    constructor(noteText: String, description: String = "") : this(Note.counterId, noteText, description)

    companion object {
        private var counterId: Int = 0
            get() {
                field++
                return field
            }
    }
}