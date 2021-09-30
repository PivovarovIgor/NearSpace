package ru.brauer.nearspace.data.network

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.brauer.nearspace.BuildConfig
import ru.brauer.nearspace.data.network.response.ApodResponse
import ru.brauer.nearspace.data.network.response.marsplanet.MarsRoverPhotoResponse

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

    fun getApod(date: String?, callbackResponse: CallbackResponse<ApodResponse>) {

        val callback = getCallback(callbackResponse)

        nasaAPI
            .getApod(BuildConfig.NASA_API_KEY, date)
            .enqueue(callback)
    }

    fun getCuriosityPhotos(
        date: String?,
        callBackResponse: CallbackResponse<MarsRoverPhotoResponse>
    ) {

        val callback = getCallback(callBackResponse)

        nasaAPI
            .getCuriosityPhotos(BuildConfig.NASA_API_KEY, date)
            .enqueue(callback)
    }

    private fun <T> getCallback(callbackResponse: CallbackResponse<T>) =
        object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val serverResponse = response.body()
                if (response.isSuccessful && serverResponse != null) {
                    callbackResponse.notify(serverResponse)
                } else {
                    callbackResponse.notify(
                        null,
                        "code=${response.code()}\nmessage=${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callbackResponse.notify(t)
            }
        }
}