package ru.brauer.nearspace.data.network

import ru.brauer.nearspace.data.network.response.ApodResponse

interface CallbackApodResponse {
    fun notify(response: ApodResponse?, onServerFailureMessage: String? = null)
    fun notify(ex: Throwable)
}