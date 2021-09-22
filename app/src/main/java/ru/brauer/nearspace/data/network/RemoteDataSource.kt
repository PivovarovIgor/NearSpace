package ru.brauer.nearspace.data.network

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.brauer.nearspace.BuildConfig
import ru.brauer.nearspace.data.network.response.ApodResponse

private const val BASE_URL = "https://api.nasa.gov/"

class RemoteDataSource {
    private val nasaAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(NasaAPI::class.java)

    fun getApod(date: String?, callbackResponse: CallbackApodResponse) {

        val callback = object : Callback<ApodResponse> {
            override fun onResponse(call: Call<ApodResponse>, response: Response<ApodResponse>) {
                val serverResponse = response.body()
                if (response.isSuccessful && serverResponse != null) {
                    callbackResponse.notify(serverResponse)
                } else {
                    callbackResponse.notify(null, response.message())
                }
            }

            override fun onFailure(call: Call<ApodResponse>, t: Throwable) {
                callbackResponse.notify(t)
            }
        }

        nasaAPI.getApod(BuildConfig.NASA_API_KEY, date).enqueue(callback)
    }
}