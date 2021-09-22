package ru.brauer.nearspace.presentation.main

import ru.brauer.nearspace.domain.entities.Apod

sealed class PhotoOfDayAppState {
    class Success(val apod: Apod) : PhotoOfDayAppState()
    object Loading : PhotoOfDayAppState()
    class Error(val exception: Throwable) : PhotoOfDayAppState()
}
