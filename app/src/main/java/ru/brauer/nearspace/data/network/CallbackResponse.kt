package ru.brauer.nearspace.data.network

import ru.brauer.nearspace.data.network.response.ApodResponse

interface CallbackResponse<T> {
    fun notify(response: T?, onServerFailureMessage: String? = null)
    fun notify(ex: Throwable)
}