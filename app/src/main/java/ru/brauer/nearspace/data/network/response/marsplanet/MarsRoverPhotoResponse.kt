package ru.brauer.nearspace.data.network.response.marsplanet


import com.google.gson.annotations.SerializedName

data class MarsRoverPhotoResponse(
    @SerializedName("photos")
    val photos: List<Photo>?
)