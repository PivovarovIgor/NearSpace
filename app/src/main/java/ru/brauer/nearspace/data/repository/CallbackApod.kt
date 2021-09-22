package ru.brauer.nearspace.data.repository

import ru.brauer.nearspace.domain.entities.Apod

interface CallbackApod {
        fun notify(response: Apod?, onServerFailureMessage: String? = null)
        fun notify(ex: Throwable)
}