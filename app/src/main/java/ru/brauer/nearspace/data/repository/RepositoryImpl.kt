package ru.brauer.nearspace.data.repository

import ru.brauer.nearspace.data.network.CallbackResponse
import ru.brauer.nearspace.data.network.RemoteDataSource
import ru.brauer.nearspace.data.network.response.ApodResponse
import ru.brauer.nearspace.domain.entities.Apod
import ru.brauer.nearspace.domain.repository.CallbackApod
import ru.brauer.nearspace.domain.repository.Repository

class RepositoryImpl(private val remoteDataSource: RemoteDataSource = RemoteDataSource()) :
    Repository {

    override fun getData(date: String?, callbackApod: CallbackApod) {

        val callbackApod = object : CallbackResponse<ApodResponse> {
            override fun notify(response: ApodResponse?, onServerFailureMessage: String?) {
                callbackApod.notify(response?.toBusiness(), onServerFailureMessage)
            }

            override fun notify(ex: Throwable) {
                callbackApod.notify(ex)
            }

        }

        remoteDataSource.getApod(date, callbackApod)
    }
}

private fun ApodResponse.toBusiness() = Apod(
    mediaType = mediaType ?: "",
    url = url ?: "",
    photoDescription = explanation ?: "",
    title = title ?: ""
)