package ru.brauer.nearspace.data.network

import ru.brauer.nearspace.data.network.response.marsplanet.MarsRoverPhotoResponse

interface CallbackMarsRoverPhotoResponse {
    fun notify(response: MarsRoverPhotoResponse?, onServerFailureMessage: String? = null)
    fun notify(ex: Throwable)
}