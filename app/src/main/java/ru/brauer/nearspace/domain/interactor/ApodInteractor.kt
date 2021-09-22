package ru.brauer.nearspace.domain.interactor

import ru.brauer.nearspace.domain.entities.Apod

interface ApodInteractor {
    fun getApod(date: Long? = null, callbackApod: CallbackApod)

    interface CallbackApod {
        fun notify(apod: Apod?, onServerFailureMessage: String?, ex: Throwable?)
    }
}