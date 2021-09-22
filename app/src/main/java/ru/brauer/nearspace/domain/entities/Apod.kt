package ru.brauer.nearspace.domain.entities

data class Apod(
    val mediaType: String,
    val url: String,
    val photoDescription: String,
    val title: String
)
