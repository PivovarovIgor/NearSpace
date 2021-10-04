package ru.brauer.nearspace.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(val noteText: String) : Parcelable