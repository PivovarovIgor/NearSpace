package ru.brauer.nearspace.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.brauer.nearspace.data.network.response.ApodResponse

interface NasaAPI {
    @GET("planetary/apod")
    fun getApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String?
    ): Call<ApodResponse>
}