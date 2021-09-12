package ru.brauer.nearspace.domain.repository

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.brauer.nearspace.BuildConfig
import ru.brauer.nearspace.domain.repository.dto.ApodDTO

class RemoteDataSource {
    private val nasaAPI = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(NasaAPI::class.java)

    fun getApod(date: String?, callback: Callback<ApodDTO>) {
        nasaAPI.getApod(BuildConfig.NASA_API_KEY, date).enqueue(callback)
    }
}