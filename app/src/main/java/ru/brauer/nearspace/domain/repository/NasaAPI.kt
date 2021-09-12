package ru.brauer.nearspace.domain.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.brauer.nearspace.domain.repository.dto.ApodDTO

interface NasaAPI {
    @GET("planetary/apod")
    fun getApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String?
    ): Call<ApodDTO>
}