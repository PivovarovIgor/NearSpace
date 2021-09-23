package ru.brauer.nearspace.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.brauer.nearspace.data.network.response.ApodResponse
import ru.brauer.nearspace.data.network.response.marsplanet.MarsRoverPhotoResponse

interface NasaAPI {
    @GET("planetary/apod")
    fun getApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String?
    ): Call<ApodResponse>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getCuriosityPhotos(
        @Query("api_key") apiKey: String,
        @Query("earth_date") earthDate: String?
    ): Call<MarsRoverPhotoResponse>
}